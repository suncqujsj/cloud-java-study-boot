package com.bob.boot.thread.pool;

/**
 * 
 * @author bob
 *
 */
public interface IJobThreadPool {
	public boolean addJobToThreadPoolQueue(IJob j);

	public void finishAllJobs();
}