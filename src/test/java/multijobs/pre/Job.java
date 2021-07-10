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
public class Job implements IJob<String, Person>, ICallback<String, Person> {

    @Override
    public Person defaultValue() {
        return new Person("default Person0");
    }

	@Override
	public Person action(String object, Map<String, JobWrapper> allJobWrappers) {
		try {
            Thread.sleep(1000);
//            while(true)
//            {
//            	Thread.sleep(1000);
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Person("Person0");
	}

    
    @Override
    public void begin() {
        //System.out.println(Thread.currentThread().getName() + "- start --" + System.currentTimeMillis());
    }

    @Override
    public void result(boolean success, String param, JobResult<Person> jobResult) {
        System.out.println("job0 的结果是：" + jobResult.getResult());
    }
}