package multijobs.parallel;


import java.util.Map;

import com.bob.boot.thread.CurrentTimeUtil;
import com.bob.boot.thread.multijobs.callback.ICallback;
import com.bob.boot.thread.multijobs.callback.IJob;
import com.bob.boot.thread.multijobs.job.JobResult;
import com.bob.boot.thread.multijobs.job.JobWrapper;

/**
 * @author bob
 */
public class Job1 implements IJob<String, String>, ICallback<String, String> {
    private long sleepTime = 1000;

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public String action(String object, Map<String, JobWrapper> allJobWrappers) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "result = " + CurrentTimeUtil.currentTimeMillis() + "---param = " + object + " from 1";
    }

    @Override
    public String defaultValue() {
        return "job1--default";
    }

    @Override
    public void begin() {
        //System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, String param, JobResult<String> JobResult) {
        if (success) {
            System.out.println("callback job1 success--" + CurrentTimeUtil.currentTimeMillis() + "----" + JobResult.getResult()
                    + "-threadName:" +Thread.currentThread().getName());
        } else {
            System.err.println("callback job1 failure--" + CurrentTimeUtil.currentTimeMillis() + "----"  + JobResult.getResult()
                    + "-threadName:" +Thread.currentThread().getName());
        }
    }

}
