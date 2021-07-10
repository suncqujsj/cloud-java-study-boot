package com.bob.boot.thread.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * 
 * @author bob
 *
 */
public class JobProcessor implements Runnable {
	private static final Logger logger = Logger.getLogger(JobProcessor.class);

	// 任务队列
	private BlockingQueue<IJob> jobQueue;

	// 是否处理标志
	private volatile boolean keepProcessing;

	public JobProcessor(BlockingQueue<IJob> queue) {
		jobQueue = queue;
		keepProcessing = true;
	}

	@Override
	public void run() {
		while (keepProcessing || !jobQueue.isEmpty()) {
			try {
				// 将任务从任务队列移除
				IJob j = jobQueue.poll(10, TimeUnit.SECONDS);
				// 并处�?
				if (j != null) {
					j.doJob();
					logger.info("JobProcessor@" + this.toString().split("@")[1] + "处理任务�?" + j.toString().split("@")[1]
							+ "," + j.getClass().getName() + "结束.");
				} else {
					// logger.info("JobQueue 10 seconds cannt get data.");
				}
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

	public void cancelExecution() {
		this.keepProcessing = false;
	}
}