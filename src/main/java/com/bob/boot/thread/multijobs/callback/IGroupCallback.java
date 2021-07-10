package com.bob.boot.thread.multijobs.callback;

import java.util.List;

import com.bob.boot.thread.multijobs.job.JobWrapper;

/**
 * 如果是异步执行整组的话，可以用这个组回调。不推荐使用
 * @author bob
 */
public interface IGroupCallback {
    /**
     * 成功后，可以从wrapper里去getJobResult
     */
    void success(List<JobWrapper> jobWrappers);
    /**
     * 失败了，也可以从jobWrappers里去getJobResult
     */
    void failure(List<JobWrapper> jobWrappers, Exception e);
}
