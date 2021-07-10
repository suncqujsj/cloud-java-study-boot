package com.bob.boot.thread.pool;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author bob
 *
 */
public class Test {
	public static void main(String[] args) throws IOException {
		JobThreadPoolImpl threadPool = new JobThreadPoolImpl(10);
		for (int i = 0; i < 10; i++) {
			threadPool.addJobToThreadPoolQueue(new PrintJob(i + ""));
		}

		threadPool.finishAllJobs();
	}
}

@Slf4j
class PrintJob implements IJob {
	private String line;

	public PrintJob(String s) {
		line = s;
	}

	public void doJob() {
		for (int i = 0; i < 10; i++) {
			log.info(Thread.currentThread().getName() + " consuming :" + line + ":" + i);
		}
	}
}