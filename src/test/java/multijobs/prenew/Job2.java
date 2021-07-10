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
public class Job2 implements IJob<Person, String>, ICallback<Person, String> {

    @Override
    public String action(Person object, Map<String, JobWrapper> allJobWrappers) {
        System.out.println("-----------------");
        System.out.println("par1的执行结果是： " + allJobWrappers.get("second").getJobResult());
        System.out.println("取par1的结果作为自己的入参，并将par1的结果加上一些东西");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Person Person1 = (Person) allJobWrappers.get("second").getJobResult().getResult();
        return Person1.getName() + " job2 add";
    }

    @Override
    public String defaultValue() {
        return "default";
    }

    @Override
    public void begin() {
        //System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, Person param, JobResult<String> JobResult) {
        System.out.println("job2 的结果是：" + JobResult.getResult());
    }

}
