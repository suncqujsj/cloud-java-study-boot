package com.bob.boot.thread.multijobs.callback;

import java.util.Map;

import com.bob.boot.thread.multijobs.job.JobWrapper;

/**
 * 每个最小执行单元需要实现该接口
 *
 * @author bob
 */
@FunctionalInterface
public interface IJob<T, V> {
    /**
     * 在这里做耗时操作，如rpc请求、IO等
     *
     * @param object      object
     * @param allJobWrappers 任务包装
     */
    V action(T object, Map<String, JobWrapper> allJobWrappers);

    /**
     * 超时、异常时，返回的默认值
     *
     * @return 默认值
     */
    default V defaultValue() {
        return null;
    }
}
