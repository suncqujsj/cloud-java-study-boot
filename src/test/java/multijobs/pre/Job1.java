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
public class Job1 implements IJob<JobResult<Person>, Person>, ICallback<JobResult<Person>, Person> {

    @Override
    public Person action(JobResult<Person> result, Map<String, JobWrapper> allWrappers) {
        System.out.println("Job1的入参来自于Job0： " + result.getResult());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Person("Person1");
    }


    @Override
    public Person defaultValue() {
        return new Person("default Person1");
    }

    @Override
    public void begin() {
        //System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, JobResult<Person> param, JobResult<Person> JobResult) {
        System.out.println("job1的结果是：" + JobResult.getResult());
    }

}
