package com.bob.boot.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author bob
 *
 */
public class VolatileSynchronizedMyself{

	private volatile static int counter1 = 0;
	private static int counter2 = 0;
	public static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {

    	// 保证所有线程执行完毕.
        final CountDownLatch cdl = new CountDownLatch(10);
		for (int i = 0; i < 10; i++) {
			new Thread(()->{
				for (int j = 0; j < 1000; j++) {
					counter1 ++;
					counter2 ++;
					Counters.increment();
					atomicInteger.incrementAndGet();
				}
				cdl.countDown();
			}).start();
		}
		
		try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		System.out.println("counter1:"+counter1);
		System.out.println("counter2:"+counter2);
		System.out.println("counter:"+Counters.getIncrement());
		System.out.println("atomicInteger:"+atomicInteger.get());
	}
}

class Counters
{
	private static int counter = 0;
	
	public static synchronized void increment()
	{
		counter ++;
	}
	
	public static synchronized int getIncrement()
	{
		return counter;
	}
}