package com.bob.boot.thread.multijobs.job;

/**
 * job执行结果
 */
public class JobResult<V> {
	/**
	 * job异常情况
	 */
	private Exception ex;
    /**
     * job执行结果
     */
    private V result;
    /**
     * job执行结果状态
     */
    private ResultState resultState;
    

    public JobResult(V result, ResultState resultState) {
        this(result, resultState, null);
    }

    public JobResult(V result, ResultState resultState, Exception ex) {
        this.result = result;
        this.resultState = resultState;
        this.ex = ex;
    }

    public static <V> JobResult<V> defaultResult() {
        return new JobResult<>(null, ResultState.DEFAULT);
    }

    @Override
    public String toString() {
        return "JobResult{" +
                "result=" + result +
                ", resultState=" + resultState +
                ", ex=" + ex +
                '}';
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    public V getResult() {
        return result;
    }

    public void setResult(V result) {
        this.result = result;
    }

    public ResultState getResultState() {
        return resultState;
    }

    public void setResultState(ResultState resultState) {
        this.resultState = resultState;
    }
}
