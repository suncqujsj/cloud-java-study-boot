package multijobs.pre;


import java.util.Map;

import com.bob.boot.entity.Person;
import com.bob.boot.thread.multijobs.callback.ICallback;
import com.bob.boot.thread.multijobs.callback.IJob;
import com.bob.boot.thread.multijobs.job.JobResult;
import com.bob.boot.thread.multijobs.job.JobWrapper;

/**
 * @author bob
 */
public class Job2 implements IJob<JobResult<Person>, String>, ICallback<JobResult<Person>, String> {

    @Override
    public String action(JobResult<Person> result, Map<String, JobWrapper> allWrappers) {
        System.out.println("job2的入参来自于job1： " + result.getResult());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result.getResult().getName();
    }


    @Override
    public String defaultValue() {
        return "default person2";
    }

    @Override
    public void begin() {
        //System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, JobResult<Person> param, JobResult<String> JobResult) {
        System.out.println("job2 的结果是：" + JobResult.getResult());
    }

}
