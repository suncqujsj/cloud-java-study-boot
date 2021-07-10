package com.bob.boot.thread.multijobs.job;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import com.bob.boot.thread.CurrentTimeUtil;
import com.bob.boot.thread.multijobs.callback.DefaultCallback;
import com.bob.boot.thread.multijobs.callback.ICallback;
import com.bob.boot.thread.multijobs.callback.IJob;
import com.bob.boot.thread.multijobs.exception.JumpOverException;

/**
 * 对每个job及callback进行包装，一对一
 *
 * @author bob
 */
public class JobWrapper<T, V> {
	/**
	 * 该wrapper的唯一标识
	 */
	private String id;
	/**
	 * job将来要处理的param
	 */
	private T param;
	private IJob<T, V> job;
	private ICallback<T, V> callback;
	/**
	 * 在自己后面的job，如果没有，自己就是末尾；如果有一个，就是串行；如果有多个，有几个就需要开几个线程
	 * </p>
	 * -------2 1 -------3 如1后面有2、3
	 */
	private List<JobWrapper> nextJobs;
	/**
	 * 依赖的jobWrappers，有2种情况，1:必须依赖的全部完成后，才能执行自己 2:依赖的任何一个、多个完成了，就可以执行自己
	 * 通过must字段来控制是否依赖项必须完成 1 -------3 2 1、2执行完毕后才能执行3
	 */
	private List<PreJobWrapper> preJobWrappers;
	/**
	 * 标记该事件是否已经被处理过了，譬如已经超时返回false了，后续rpc又收到返回值了，则不再二次回调
	 * 经试验,volatile并不能保证"同一毫秒"内,多线程对该值的修改和拉取
	 * <p>
	 * 1-finish, 2-error, 3-jobing
	 */
	private AtomicInteger jobState = new AtomicInteger(0);
	/**
	 * 该map存放所有jobWrapper的id和jobWrapper映射
	 */
	private Map<String, JobWrapper> idToJobMappersMap;
	/**
	 * 也是个钩子变量，用来存临时的结果
	 */
	private volatile JobResult<V> jobResult = JobResult.defaultResult();
	/**
	 * 是否在执行自己前，去校验nextJobWrapper的执行结果
	 * <p>
	 * 1 4 -------3 2 如这种在4执行前，可能3已经执行完毕了（被2执行完后触发的），那么4就没必要执行了。
	 * 注意，该属性仅在nextJobWrapper数量<=1时有效，>1时的情况是不存在的
	 */
	private volatile boolean needCheckNextJobWrapperResult = true;

	private static final int FINISH = 1;
	private static final int ERROR = 2;
	private static final int JOBING = 3;
	private static final int INIT = 0;

	private JobWrapper(String id, IJob<T, V> job, T param, ICallback<T, V> callback) {
		if (job == null) {
			throw new NullPointerException("async.job is null");
		}
		this.job = job;
		this.param = param;
		this.id = id;
		// 允许不设置回调
		if (callback == null) {
			callback = new DefaultCallback<>();
		}
		this.callback = callback;
	}

	/**
	 * 开始工作 preJobWrapper代表这次job是由哪个上游jobWrapper发起的
	 */
	private void job(ExecutorService executorService, JobWrapper preJobWrapper, long remainTime,
			Map<String, JobWrapper> idToJobMappersMap) {
		this.idToJobMappersMap = idToJobMappersMap;
		// 将自己放到所有jobWrapper的集合里去
		idToJobMappersMap.put(id, this);
		long currentTimeMillis = CurrentTimeUtil.currentTimeMillis();
		// 总的已经超时了，就快速失败，进行下一个
		if (remainTime <= 0) {
			fastFail(INIT, null);
			beginNextJob(executorService, currentTimeMillis, remainTime);
			return;
		}
		// 如果自己已经执行过了。
		// 可能有多个依赖，其中的一个依赖已经执行完了，并且自己也已开始执行或执行完毕。当另一个依赖执行完毕，又进来该方法时，就不重复处理了
		if (getState() == FINISH || getState() == ERROR) {
			beginNextJob(executorService, currentTimeMillis, remainTime);
			return;
		}

		// 如果在执行前需要校验nextJobWrapper的状态
		if (needCheckNextJobWrapperResult) {
			// 如果自己的next链上有已经出结果或已经开始执行的任务了，自己就不用继续了
			if (!checkNextJobWrapperResult()) {
				fastFail(INIT, new JumpOverException());
				beginNextJob(executorService, currentTimeMillis, remainTime);
				return;
			}
		}

		// 如果没有任何依赖，说明自己就是第一批要执行的
		if (preJobWrappers == null || preJobWrappers.size() == 0) {
			doing();
			beginNextJob(executorService, currentTimeMillis, remainTime);
			return;
		}

		/*
		 * 如果有前方依赖，存在两种情况 一种是前面只有一个jobWrapper。即 A -> B 一种是前面有多个jobWrapper。A C D ->
		 * B。需要A、C、D都完成了才能轮到B。但是无论是A执行完，还是C执行完，都会去唤醒B。 所以需要B来做判断，必须A、C、D都完成，自己才能执行
		 */

		// 只有一个依赖
		if (preJobWrappers.size() == 1) {
			doPreOneJob(preJobWrapper);
			beginNextJob(executorService, currentTimeMillis, remainTime);
		} else {
			// 有多个依赖时
			doPreJobs(executorService, preJobWrappers, preJobWrapper, currentTimeMillis, remainTime);
		}

	}

	public void job(ExecutorService executorService, long remainTime, Map<String, JobWrapper> forParamUseWrappers) {
		job(executorService, null, remainTime, forParamUseWrappers);
	}

	/**
	 * 总控制台超时，停止所有任务
	 */
	public void stopNow() {
		if (getState() == INIT || getState() == JOBING) {
			fastFail(getState(), null);
		}
	}

	/**
	 * 判断自己下游链路上，是否存在已经出结果的或已经开始执行的 如果没有返回true，如果有返回false
	 */
	private boolean checkNextJobWrapperResult() {
		// 如果自己就是最后一个，或者后面有并行的多个，就返回true
		if (nextJobs == null || nextJobs.size() != 1) {
			return getState() == INIT;
		}
		JobWrapper nextJobWrapper = nextJobs.get(0);
		boolean jobState = nextJobWrapper.getState() == INIT;
		// 继续校验自己的next的状态
		return jobState && nextJobWrapper.checkNextJobWrapperResult();
	}

	/**
	 * 进行下一个任务
	 */
	private void beginNextJob(ExecutorService executorService, long now, long remainTime) {
		// 花费的时间
		long costTime = CurrentTimeUtil.currentTimeMillis() - now;
		if (nextJobs == null) {
			return;
		}
		if (nextJobs.size() == 1) {
			nextJobs.get(0).job(executorService, JobWrapper.this, remainTime - costTime, idToJobMappersMap);
			return;
		}
		CompletableFuture[] futures = new CompletableFuture[nextJobs.size()];
		for (int i = 0; i < nextJobs.size(); i++) {
			int finalI = i;
			futures[i] = CompletableFuture.runAsync(() -> nextJobs.get(finalI).job(executorService, JobWrapper.this,
					remainTime - costTime, idToJobMappersMap), executorService);
		}
		try {
			CompletableFuture.allOf(futures).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	private void doPreOneJob(JobWrapper preJobWrapper) {
		if (ResultState.TIMEOUT == preJobWrapper.getJobResult().getResultState()) {
			jobResult = defaultResult();
			fastFail(INIT, null);
		} else if (ResultState.EXCEPTION == preJobWrapper.getJobResult().getResultState()) {
			jobResult = defaultExResult(preJobWrapper.getJobResult().getEx());
			fastFail(INIT, null);
		} else {
			// 前面任务正常完毕了，该自己了
			doing();
		}
	}

	private synchronized void doPreJobs(ExecutorService executorService, List<PreJobWrapper> preJobWrappers,
			JobWrapper fromWrapper, long now, long remainTime) {
		boolean nowDependIsMust = false;
		// 创建必须完成的上游jobWrapper集合
		Set<PreJobWrapper> shouldWrapper = new HashSet<>();
		for (PreJobWrapper preJobWrapper : preJobWrappers) {
			if (preJobWrapper.isShouldFlag()) {
				shouldWrapper.add(preJobWrapper);
			}
			if (preJobWrapper.getPreJobWrapper().equals(fromWrapper)) {
				nowDependIsMust = preJobWrapper.isShouldFlag();
			}
		}

		// 如果全部是不必须的条件，那么只要到了这里，就执行自己。
		if (shouldWrapper.size() == 0) {
			if (ResultState.TIMEOUT == fromWrapper.getJobResult().getResultState()) {
				fastFail(INIT, null);
			} else {
				doing();
			}
			beginNextJob(executorService, now, remainTime);
			return;
		}

		// 如果存在需要必须完成的，且fromWrapper不是必须的，就什么也不干
		if (!nowDependIsMust) {
			return;
		}

		// 如果fromWrapper是必须的
		boolean existNoFinish = false;
		boolean hasError = false;
		// 先判断前面必须要执行的依赖任务的执行结果，如果有任何一个失败，那就不用走action了，直接给自己设置为失败，进行下一步就是了
		for (PreJobWrapper preJobWrapper : shouldWrapper) {
			JobWrapper jobWrapper = preJobWrapper.getPreJobWrapper();
			JobResult tempJobResult = jobWrapper.getJobResult();
			// 为null或者isJobing，说明它依赖的某个任务还没执行到或没执行完
			if (jobWrapper.getState() == INIT || jobWrapper.getState() == JOBING) {
				existNoFinish = true;
				break;
			}
			if (ResultState.TIMEOUT == tempJobResult.getResultState()) {
				jobResult = defaultResult();
				hasError = true;
				break;
			}
			if (ResultState.EXCEPTION == tempJobResult.getResultState()) {
				jobResult = defaultExResult(jobWrapper.getJobResult().getEx());
				hasError = true;
				break;
			}

		}
		// 只要有失败的
		if (hasError) {
			fastFail(INIT, null);
			beginNextJob(executorService, now, remainTime);
			return;
		}

		// 如果上游都没有失败，分为两种情况，一种是都finish了，一种是有的在jobing
		// 都finish的话
		if (!existNoFinish) {
			// 上游都finish了，进行自己
			doing();
			beginNextJob(executorService, now, remainTime);
			return;
		}
	}

	/**
	 * 执行自己的job.具体的执行是在另一个线程里,但判断阻塞超时是在job线程
	 */
	private void doing() {
		// 阻塞取结果
		jobResult = jobDoJob();
	}

	/**
	 * 快速失败
	 */
	private boolean fastFail(int expect, Exception e) {
		// 试图将它从expect状态,改成Error
		if (!compareAndSetState(expect, ERROR)) {
			return false;
		}

		// 尚未处理过结果
		if (checkIsNullResult()) {
			if (e == null) {
				jobResult = defaultResult();
			} else {
				jobResult = defaultExResult(e);
			}
		}

		callback.result(false, param, jobResult);
		return true;
	}

	/**
	 * 具体的单个jober执行任务
	 */
	private JobResult<V> jobDoJob() {
		// 避免重复执行
		if (!checkIsNullResult()) {
			return jobResult;
		}
		try {
			// 如果已经不是init状态了，说明正在被执行或已执行完毕。这一步很重要，可以保证任务不被重复执行
			if (!compareAndSetState(INIT, JOBING)) {
				return jobResult;
			}

			callback.begin();

			// 执行耗时操作
			V resultValue = job.action(param, idToJobMappersMap);

			// 如果状态不是在jobing,说明别的地方已经修改了
			if (!compareAndSetState(JOBING, FINISH)) {
				return jobResult;
			}

			jobResult.setResultState(ResultState.SUCCESS);
			jobResult.setResult(resultValue);
			// 回调成功
			callback.result(true, param, jobResult);

			return jobResult;
		} catch (Exception e) {
			// 避免重复回调
			if (!checkIsNullResult()) {
				return jobResult;
			}
			fastFail(JOBING, e);
			return jobResult;
		}
	}

	public JobResult<V> getJobResult() {
		return jobResult;
	}

	public List<JobWrapper> getNextJobWrappers() {
		return nextJobs;
	}

	public void setParam(T param) {
		this.param = param;
	}

	private boolean checkIsNullResult() {
		return ResultState.DEFAULT == jobResult.getResultState();
	}

	private void addPreJobWrapper(JobWrapper<?, ?> jobWrapper, boolean should) {
		addPreJobWrapper(new PreJobWrapper(jobWrapper, should));
	}

	private void addPreJobWrapper(PreJobWrapper preJobWrapper) {
		if (preJobWrappers == null) {
			preJobWrappers = new ArrayList<>();
		}
		// 如果依赖的是重复的同一个，就不重复添加了
		for (PreJobWrapper jobWrapper : preJobWrappers) {
			if (jobWrapper.equals(preJobWrapper)) {
				return;
			}
		}
		preJobWrappers.add(preJobWrapper);
	}

	private void addNextJobWrapper(JobWrapper<?, ?> jobWrapper) {
		if (nextJobs == null) {
			nextJobs = new ArrayList<>();
		}
		// 避免添加重复
		for (JobWrapper<?, ?> jobWrapperTmp : nextJobs) {
			if (jobWrapper.equals(jobWrapperTmp)) {
				return;
			}
		}
		nextJobs.add(jobWrapper);
	}

	//以下两个在原xm是没注释的，但也没有被调用
//	private void addNextWrappers(List<JobWrapper<?, ?>> jobWrappers) {
//		if (jobWrappers == null) {
//			return;
//		}
//		for (JobWrapper<?, ?> jobWrapper : jobWrappers) {
//			addNextJobWrapper(jobWrapper);
//		}
//	}
//
//	private void addPreJobWrappers(List<PreJobWrapper> preJobWrappers) {
//		if (preJobWrappers == null) {
//			return;
//		}
//		for (PreJobWrapper jobWrapper : preJobWrappers) {
//			addPreJobWrapper(jobWrapper);
//		}
//	}

	private JobResult<V> defaultResult() {
		jobResult.setResultState(ResultState.TIMEOUT);
		jobResult.setResult(job.defaultValue());
		return jobResult;
	}

	private JobResult<V> defaultExResult(Exception ex) {
		jobResult.setResultState(ResultState.EXCEPTION);
		jobResult.setResult(job.defaultValue());
		jobResult.setEx(ex);
		return jobResult;
	}

	private int getState() {
		return jobState.get();
	}

	public String getId() {
		return id;
	}

	private boolean compareAndSetState(int expect, int update) {
		return this.jobState.compareAndSet(expect, update);
	}

	private void setNeedCheckNextWrapperResult(boolean needCheckNextWrapperResult) {
		this.needCheckNextJobWrapperResult = needCheckNextWrapperResult;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JobWrapper<?, ?> that = (JobWrapper<?, ?>) o;
		return needCheckNextJobWrapperResult == that.needCheckNextJobWrapperResult && Objects.equals(param, that.param)
				&& Objects.equals(job, that.job) && Objects.equals(callback, that.callback)
				&& Objects.equals(nextJobs, that.nextJobs) && Objects.equals(preJobWrappers, that.preJobWrappers)
				&& Objects.equals(jobState, that.jobState) && Objects.equals(jobResult, that.jobResult);
	}

	@Override
	public int hashCode() {
		return Objects.hash(param, job, callback, nextJobs, preJobWrappers, jobState, jobResult,
				needCheckNextJobWrapperResult);
	}

	public static class Builder<W, C> {
		/**
		 * 该wrapper的唯一标识
		 */
		private String id = UUID.randomUUID().toString();
		/**
		 * jober将来要处理的param
		 */
		private W param;
		private IJob<W, C> job;
		private ICallback<W, C> callback;
		/**
		 * 自己后面的所有
		 */
		private List<JobWrapper<?, ?>> nextJobWrappers;
		/**
		 * 自己依赖的所有
		 */
		private List<PreJobWrapper> preJobWrappers;
		/**
		 * 存储强依赖于自己的jobWrapper集合
		 */
		private Set<JobWrapper<?, ?>> selfIsShouldJobWrappersSet;

		private boolean needCheckNextJobWrapperResult = true;

		public Builder<W, C> job(IJob<W, C> jober) {
			this.job = jober;
			return this;
		}

		public Builder<W, C> param(W w) {
			this.param = w;
			return this;
		}

		public Builder<W, C> id(String id) {
			if (id != null) {
				this.id = id;
			}
			return this;
		}

		public Builder<W, C> needCheckNextWrapperResult(boolean needCheckNextWrapperResult) {
			this.needCheckNextJobWrapperResult = needCheckNextWrapperResult;
			return this;
		}

		public Builder<W, C> callback(ICallback<W, C> callback) {
			this.callback = callback;
			return this;
		}

		public Builder<W, C> pre(JobWrapper<?, ?>... wrappers) {
			if (wrappers == null) {
				return this;
			}
			for (JobWrapper<?, ?> wrapper : wrappers) {
				pre(wrapper);
			}
			return this;
		}

		public Builder<W, C> pre(JobWrapper<?, ?> wrapper) {
			return pre(wrapper, true);
		}

		public Builder<W, C> pre(JobWrapper<?, ?> wrapper, boolean isMust) {
			if (wrapper == null) {
				return this;
			}
			PreJobWrapper dependWrapper = new PreJobWrapper(wrapper, isMust);
			if (preJobWrappers == null) {
				preJobWrappers = new ArrayList<>();
			}
			preJobWrappers.add(dependWrapper);
			return this;
		}

		public Builder<W, C> next(JobWrapper<?, ?> wrapper) {
			return next(wrapper, true);
		}

		public Builder<W, C> next(JobWrapper<?, ?> wrapper, boolean selfIsShould) {
			if (nextJobWrappers == null) {
				nextJobWrappers = new ArrayList<>();
			}
			nextJobWrappers.add(wrapper);

			// 强依赖自己
			if (selfIsShould) {
				if (selfIsShouldJobWrappersSet == null) {
					selfIsShouldJobWrappersSet = new HashSet<>();
				}
				selfIsShouldJobWrappersSet.add(wrapper);
			}
			return this;
		}

		public Builder<W, C> next(JobWrapper<?, ?>... jobWrappers) {
			if (jobWrappers == null) {
				return this;
			}
			for (JobWrapper<?, ?> jobWrapper : jobWrappers) {
				next(jobWrapper);
			}
			return this;
		}

		public JobWrapper<W, C> build() {
			JobWrapper<W, C> jobWrapper = new JobWrapper<>(id, job, param, callback);
			jobWrapper.setNeedCheckNextWrapperResult(needCheckNextJobWrapperResult);
			if (preJobWrappers != null) {
				for (PreJobWrapper jobWrapperTmp : preJobWrappers) {
					jobWrapperTmp.getPreJobWrapper().addNextJobWrapper(jobWrapper);
					jobWrapper.addPreJobWrapper(jobWrapperTmp);
				}
			}
			if (nextJobWrappers != null) {
				for (JobWrapper<?, ?> tmpWrapper : nextJobWrappers) {
					boolean should = false;
					if (selfIsShouldJobWrappersSet != null && selfIsShouldJobWrappersSet.contains(tmpWrapper)) {
						should = true;
					}
					tmpWrapper.addPreJobWrapper(jobWrapper, should);
					jobWrapper.addNextJobWrapper(tmpWrapper);
				}
			}

			return jobWrapper;
		}

	}
}
