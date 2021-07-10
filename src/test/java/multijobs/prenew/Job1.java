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
public class Job1 implements IJob<String, Person>, ICallback<String, Person> {

    @Override
    public Person action(String object, Map<String, JobWrapper> allJobWrappers) {
        System.out.println("-----------------");
        System.out.println("获取par0的执行结果： " + allJobWrappers.get("first").getJobResult());
        System.out.println("取par0的结果作为自己的入参，并将par0的结果加上一些东西");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Person Person0 = (Person) allJobWrappers.get("first").getJobResult().getResult();
        return new Person(Person0.getName() + " job1 add");
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
    public void result(boolean success, String param, JobResult<Person> jobResult) {
        System.out.println("job1 的结果是：" + jobResult.getResult());
    }
}
