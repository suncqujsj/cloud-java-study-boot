package com.bob.boot.thread.multijobs.callback;

import java.util.List;

import com.bob.boot.thread.multijobs.job.JobWrapper;

/**
 * @author bob
 * @version 1.0
 */
public class DefaultGroupCallback implements IGroupCallback {
	/**
     * 成功后，可以从wrapper里去getJobResult
     */
    public void success(List<JobWrapper> jobWrappers)
    {
    		
    }
    
    /**
     * 失败了，也可以从jobWrappers里去getJobResult
     */
    public void failure(List<JobWrapper> jobWrappers, Exception e)
    {
    	
    }
}
