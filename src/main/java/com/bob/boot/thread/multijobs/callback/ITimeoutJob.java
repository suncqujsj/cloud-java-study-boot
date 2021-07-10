package com.bob.boot.thread.multijobs.callback;

/**
 * @author bob
 * @version 1.0
 */
public interface ITimeoutJob<T, V> extends IJob<T, V> {
    /**
     * 每个worker都可以设置超时时间
     * @return 毫秒超时时间
     */
    long timeOut();

    /**
     * 是否开启单个执行单元的超时功能（有时是一个group设置个超时，而不具备关心单个job的超时）
     * <p>注意，如果开启了单个执行单元的超时检测，将使线程池数量多出一倍</p>
     * @return 是否开启
     */
    boolean enableTimeOut();
}
