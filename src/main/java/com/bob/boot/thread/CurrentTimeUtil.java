package com.bob.boot.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用于解决高并发下System.currentTimeMillis卡顿
 * 
 * @author bob
 */
public class CurrentTimeUtil {

	private final int interTimes;

	private final AtomicLong currentTimeMillis;

	private static class InstanceHolder {
		private static final CurrentTimeUtil INSTANCE = new CurrentTimeUtil(1);
	}

	private CurrentTimeUtil(int period) {
		this.interTimes = period;
		this.currentTimeMillis = new AtomicLong(System.currentTimeMillis());
		scheduleClockUpdating();
	}

	private static CurrentTimeUtil instance() {
		return InstanceHolder.INSTANCE;
	}

	private void scheduleClockUpdating() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
			Thread thread = new Thread(runnable, "CurrentTimeUtil");
			thread.setDaemon(true);
			return thread;
		});
		scheduler.scheduleAtFixedRate(() -> currentTimeMillis.set(System.currentTimeMillis()), interTimes, interTimes, TimeUnit.MILLISECONDS);
	}

	private long innerCurrentTimeMillis() {
		return currentTimeMillis.get();
	}

	/**
	 * 用来替换原来的System.currentTimeMillis()
	 */
	public static long currentTimeMillis() {
		return instance().innerCurrentTimeMillis();
	}

	public static void main(String[] args) {
		Runnable runnable = new Runnable() {
	            public void run() {
	            	System.out.println("当前时间：" + currentTimeMillis());
	            };
	        };
		
		 ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	     // 1表示时间单位的数值 TimeUnit.SECONDS  延时单位为秒
	     service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.MILLISECONDS);
	}
}