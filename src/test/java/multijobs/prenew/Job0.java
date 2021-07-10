package multijobs.prenew;


import java.util.Map;

import com.bob.boot.entity.Person;
import com.bob.boot.thread.multijobs.callback.ICallback;
import com.bob.boot.thread.multijobs.callback.IJob;
import com.bob.boot.thread.multijobs.job.JobResult;
import com.bob.boot.thread.multijobs.job.JobWrapper;

/**
 * @author bob
 */
public class Job0 implements IJob<String, Person>, ICallback<String, Person> {

    @Override
    public Person action(String object, Map<String, JobWrapper> allWrappers) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Person("person0");
    }


    @Override
    public Person defaultValue() {
        return new Person("default Person");
    }

    @Override
    public void begin() {
        //System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, String param, JobResult<Person> workResult) {
        System.out.println("worker0 的结果是：" + workResult.getResult());
    }

}
