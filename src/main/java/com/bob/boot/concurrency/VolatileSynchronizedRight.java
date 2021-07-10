package com.bob.boot.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

//线程安全: 当多个线程访问某个类时, 这个类始终都能表现出正确的行为, 那么就称这个类是线程安全的。
/**
 * 
 * @author bob
 *
 */
public class VolatileSynchronizedRight {

       public static int count = 0;
       public static Counter counter = new Counter();
       public static AtomicInteger atomicInteger = new AtomicInteger(0);
       volatile public static int countVolatile = 0;

    public static void main(String[] args) {
        // 保证所有线程执行完毕.
        final CountDownLatch cdl = new CountDownLatch(10);
        for(int i=0; i<10; i++) {
             new Thread() {
                 public void run() {
                     for (int j = 0; j < 1000; j++) {
                         count++;
                         counter.increment();
                         atomicInteger.getAndIncrement();
                         countVolatile++;
                    }
                    cdl.countDown();
                 }
             }.start();
        }
        
        try {
            System.out.println("主线程"+Thread.currentThread().getName()+"等待子线程执行完成...");
            cdl.await();//阻塞当前线程，直到计数器的值为0
            System.out.println("主线程"+Thread.currentThread().getName()+"开始执行...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("static count: " + count);
        System.out.println("Counter: " + counter.getValue());
        System.out.println("AtomicInteger: " + atomicInteger.intValue());
        System.out.println("countVolatile: " + countVolatile);
    }
}

class Counter {

    private int value;

    public synchronized int getValue() {
        return value;
    }

    public synchronized int increment() {
        return value++;
    }

    public synchronized int decrement() {
        return --value;
    }
}