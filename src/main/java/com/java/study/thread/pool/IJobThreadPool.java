package com.java.study.thread.pool;

public interface IJobThreadPool {
 public boolean addJobToThreadPoolQueue(IJob j);  

 public void finishAllJobs();  
}