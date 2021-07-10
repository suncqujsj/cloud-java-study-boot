package multijobs.seq;


import java.util.Map;

import com.bob.boot.thread.CurrentTimeUtil;
import com.bob.boot.thread.multijobs.callback.ICallback;
import com.bob.boot.thread.multijobs.callback.IJob;
import com.bob.boot.thread.multijobs.job.JobResult;
import com.bob.boot.thread.multijobs.job.JobWrapper;

/**
 * @author bob
 */
public class Job2 implements IJob<String, String>, ICallback<String, String> {

    @Override
    public String action(String object, Map<String, JobWrapper> allJobWrappers) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "result = " + CurrentTimeUtil.currentTimeMillis() + "---param = " + object + " from 0";
    }

    @Override
    public String defaultValue() {
        return "job2--default";
    }

    @Override
    public void begin() {
        //System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, String param, JobResult<String> workResult) {
        if (success) {
            System.out.println("callback job2 success--" + CurrentTimeUtil.currentTimeMillis() + "----" + workResult.getResult()
                    + "-threadName:" +Thread.currentThread().getName());
        } else {
            System.err.println("callback job2 failure--" + CurrentTimeUtil.currentTimeMillis() + "----"  + workResult.getResult()
                    + "-threadName:" +Thread.currentThread().getName());
        }
    }

}
