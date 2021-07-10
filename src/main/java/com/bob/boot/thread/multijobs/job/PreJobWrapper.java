package com.bob.boot.thread.multijobs.job;

/**
 * 对依赖的jobwrapper的封装
 * @author bob
 * @version 1.0
 */
public class PreJobWrapper {
    private JobWrapper<?, ?> preJobWrapper;
    /**
     * 是否该依赖必须完成后才能执行自己.<p>
     * 因为存在一个任务，依赖于多个任务，是让这多个任务全部完成后才执行自己，还是某几个执行完毕就可以执行自己
     * 如
     * 1
     * ---3
     * 2
     * 或
     * 1---3
     * 2---3
     * 这两种就不一样，上面的就是必须12都完毕，才能3
     * 下面的就是1完毕就可以3
     */
    private boolean shouldFlag = true;

    public PreJobWrapper(JobWrapper<?, ?> preJobWrapper, boolean shouldFlag) {
        this.preJobWrapper = preJobWrapper;
        this.shouldFlag = shouldFlag;
    }

    public PreJobWrapper() {
    }

    public JobWrapper<?, ?> getPreJobWrapper() {
        return preJobWrapper;
    }

    public void setPreJobWrapper(JobWrapper<?, ?> preJobWrapper) {
        this.preJobWrapper = preJobWrapper;
    }

    public boolean isShouldFlag() {
        return shouldFlag;
    }

    public void setShouldFlag(boolean shouldFlag) {
        this.shouldFlag = shouldFlag;
    }

    @Override
    public String toString() {
        return "PreJobWrapper{" +
                "preJobWrapper=" + preJobWrapper +
                ", shouldFlag=" + shouldFlag +
                '}';
    }
}