package com.bob.boot.thread.pool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author bob
 *
 */
@Slf4j
public class JobThreadPoolImpl implements IJobThreadPool {
	private static final Logger logger = Logger.getLogger(JobThreadPoolImpl.class);

	// 任务集合
	private BlockingQueue<IJob> itemQueue = new LinkedBlockingQueue<IJob>();

	// 线程条件名称
	private ConcurrentHashMap<String, String> conditions = new ConcurrentHashMap<String, String>();

	// 线程池cache60s no execute thread auto destroy
	private ExecutorService executorService = Executors.newCachedThreadPool();

	// 任务处理器集�?
	private List<JobProcessor> jobList = new LinkedList<JobProcessor>();

	// 关闭标志
	private volatile boolean shutdownCalled = false;

	/**
	 * 初始 线程�?
	 * 
	 * @param poolSize
	 */
	public JobThreadPoolImpl(int poolSize) {
		for (int i = 0; i < poolSize; i++) {
			// 同一个itemQueue,JobProcessor是线程，poolSize个线程去处理同一个queue中的任务
			JobProcessor jobThread = new JobProcessor(itemQueue);
			jobList.add(jobThread);
			// 将任务提交到线程池中
			executorService.submit(jobThread);
		}
	}

	public void addConditions(String con) {
		conditions.put(con, con);
	}

	public String getCondition(String con) {
		return conditions.get(con);
	}

	public String delCondition(String con) {
		return conditions.remove(con);
	}

	@Override
	public boolean addJobToThreadPoolQueue(IJob j) {
		if (!shutdownCalled) {
			try {
				itemQueue.put(j);
				logger.info("任务:IJob@" + j.toString().split("@")[1] + "," + j.getClass().getName() + "入任务链接阻塞队�?.");
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void finishAllJobs() {
		logger.info("�?始置JobProcessor's keepProcessing：false.");
		for (JobProcessor j : jobList) {
			j.cancelExecution();
			logger.info("JobProcessor@" + j.toString().split("@")[1] + "'s keepProcessing置为false.");
		}
		try {
			executorService.shutdown();
			shutdownCalled = true;
		} catch (Exception e) {
			log.error("error:", e);
		}
	}
}