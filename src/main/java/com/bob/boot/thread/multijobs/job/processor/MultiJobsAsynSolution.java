package com.bob.boot.thread.multijobs.job.processor;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import com.bob.boot.thread.multijobs.callback.DefaultGroupCallback;
import com.bob.boot.thread.multijobs.callback.IGroupCallback;
import com.bob.boot.thread.multijobs.job.JobWrapper;

/**
 * 类入口，可以根据自己情况调整core线程的数量
 * @author bob
 * @version 1.0
 */
public class MultiJobsAsynSolution {
    /**
     * 默认线程池
     */
    private static final ThreadPoolExecutor DEFAULT_POOL =
            new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 1024,
                    15L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(),
                    (ThreadFactory) Thread::new);
    /**
     * 注意，这里是个static，也就是只能有一个线程池。用户自定义线程池时，也只能定义一个
     */
    private static ExecutorService executorService;

    /**
     * 出发点
     */
    public static boolean beginJob(long timeout, ExecutorService executorService, List<JobWrapper> jobWrappers) throws ExecutionException, InterruptedException {
        if(jobWrappers == null || jobWrappers.size() == 0) {
            return false;
        }
        //保存线程池变量
        MultiJobsAsynSolution.executorService = executorService;
        //定义一个map，存放所有的jobWrapper，key为jobWrapper的唯一id，value是该jobWrapper，可以从value中获取jobWrapper的result
        Map<String, JobWrapper<?,?>> allJobWrappersMap = new ConcurrentHashMap<>();
        CompletableFuture<?>[] futures = new CompletableFuture[jobWrappers.size()];
        for (int i = 0; i < jobWrappers.size(); i++) {
        	JobWrapper wrapper = jobWrappers.get(i);
            futures[i] = CompletableFuture.runAsync(() -> wrapper.job(executorService, timeout, allJobWrappersMap), executorService);
        }
        try {
            CompletableFuture.allOf(futures).get(timeout, TimeUnit.MILLISECONDS);
            return true;
        } catch (TimeoutException e) {
            Set<JobWrapper> set = new HashSet<>();
            totalJobs(jobWrappers, set);
            for (JobWrapper<?,?> wrapper : set) {
                wrapper.stopNow();
            }
            return false;
        }
    }

    /**
     * 如果想自定义线程池，请传pool。不自定义的话，就走默认的COMMON_POOL
     */
    public static boolean beginJob(long timeout, ExecutorService executorService, JobWrapper... jobWrapper) throws ExecutionException, InterruptedException {
        if(jobWrapper == null || jobWrapper.length == 0) {
            return false;
        }
        List<JobWrapper> jobWrappers =  Arrays.stream(jobWrapper).collect(Collectors.toList());
        return beginJob(timeout, executorService, jobWrappers);
    }

    /**
     * 同步阻塞,直到所有都完成,或失败
     */
    public static boolean beginJob(long timeout, JobWrapper<?,?>... JobWrapper) throws ExecutionException, InterruptedException {
        return beginJob(timeout, DEFAULT_POOL, JobWrapper);
    }

    public static void beginJobAsync(long timeout, IGroupCallback groupCallback, JobWrapper<?,?>... jobWrapper) {
        beginJobAsync(timeout, DEFAULT_POOL, groupCallback, jobWrapper);
    }

    /**
     * 异步执行,直到所有都完成,或失败后，发起回调
     */
    public static void beginJobAsync(long timeout, ExecutorService executorService, IGroupCallback groupCallback, JobWrapper<?,?>... jobWrapper) {
        if (groupCallback == null) {
            groupCallback = new DefaultGroupCallback();
        }
        IGroupCallback completeGroupCallback = groupCallback;
        if (executorService != null) {
            executorService.submit(() -> {
                try {
                    boolean success = beginJob(timeout, executorService, jobWrapper);
                    if (success) {
                        completeGroupCallback.success(Arrays.asList(jobWrapper));
                    } else {
                        completeGroupCallback.failure(Arrays.asList(jobWrapper), new TimeoutException());
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    completeGroupCallback.failure(Arrays.asList(jobWrapper), e);
                }
            });
        } else {
            DEFAULT_POOL.submit(() -> {
                try {
                    boolean success = beginJob(timeout, DEFAULT_POOL, jobWrapper);
                    if (success) {
                        completeGroupCallback.success(Arrays.asList(jobWrapper));
                    } else {
                        completeGroupCallback.failure(Arrays.asList(jobWrapper), new TimeoutException());
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    completeGroupCallback.failure(Arrays.asList(jobWrapper), e);
                }
            });
        }

    }

    /**
     * 总共多少个执行单元
     */
    private static void totalJobs(List<JobWrapper> JobWrappers, Set<JobWrapper> jobWrapperSet) {
    	jobWrapperSet.addAll(JobWrappers);
        for (JobWrapper<?,?> jobWrapper : JobWrappers) {
            if (jobWrapper.getNextJobWrappers() == null) {
                continue;
            }
            List<JobWrapper> jobWrappers = jobWrapper.getNextJobWrappers();
            totalJobs(jobWrappers, jobWrapperSet);
        }

    }

    /**
     * 关闭线程池
     */
    public static void shutDown() {
        shutDown(executorService);
    }

    /**
     * 关闭线程池
     */
    public static void shutDown(ExecutorService executorService) {
        if (executorService != null) {
            executorService.shutdown();
        } else {
            DEFAULT_POOL.shutdown();
        }
    }

    public static String getThreadCount() {
        return "activeJobCounts=" + DEFAULT_POOL.getActiveCount() +
                "  completedJobCounts " + DEFAULT_POOL.getCompletedTaskCount() +
                "  largestThreadCounts " + DEFAULT_POOL.getLargestPoolSize();
    }
}
