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
public class Job3 implements IJob<String, String>, ICallback<String, String> {
    private long sleepTime = 1000;

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }
    @Override
    public String action(String object, Map<String, JobWrapper> allWrappers) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "result = " + CurrentTimeUtil.currentTimeMillis() + "---param = " + object + " from 3";
    }


    @Override
    public String defaultValue() {
        return "job3--default";
    }

    @Override
    public void begin() {
        //System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, String param, JobResult<String> jobResult) {
        if (success) {
            System.out.println("callback job3 success--" + CurrentTimeUtil.currentTimeMillis() + "----" + jobResult.getResult()
                    + "-threadName:" +Thread.currentThread().getName());
        } else {
            System.err.println("callback job3 failure--" + CurrentTimeUtil.currentTimeMillis() + "----"  + jobResult.getResult()
                    + "-threadName:" +Thread.currentThread().getName());
        }
    }

}
