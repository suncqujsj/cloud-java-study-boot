package multijobs.pre;

import java.util.Map;

import com.bob.boot.entity.Person;
import com.bob.boot.thread.multijobs.job.JobResult;
import com.bob.boot.thread.multijobs.job.JobWrapper;
import com.bob.boot.thread.multijobs.job.processor.MultiJobsAsynSolution;

/**
 * @author bob
 * @since 2020/6/14
 */
public class LambdaTest {
    public static void main(String[] args) throws Exception {
        JobWrapper<JobResult<Person>, String> JobWrapper2 = new JobWrapper.Builder<JobResult<Person>, String>()
                .job((JobResult<Person> result, Map<String, JobWrapper> allWrappers) -> {
                    System.out.println("par2的入参来自于par1： " + result.getResult());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return result.getResult().getName();
                })
                .callback((boolean success, JobResult<Person> param, JobResult<String> JobResult) ->
                        System.out.println(String.format("thread is %s, param is %s, result is %s", Thread.currentThread().getName(), param, JobResult)))
                .id("third")
                .build();

        JobWrapper<JobResult<Person>, Person> JobWrapper1 = new JobWrapper.Builder<JobResult<Person>, Person>()
                .job((JobResult<Person> result, Map<String, JobWrapper> allWrappers) -> {
                    System.out.println("par1的入参来自于par0： " + result.getResult());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return new Person("Person1");
                })
                .callback((boolean success, JobResult<Person> param, JobResult<Person> JobResult) ->
                        System.out.println(String.format("thread is %s, param is %s, result is %s", Thread.currentThread().getName(), param, JobResult)))
                .id("second")
                .next(JobWrapper2)
                .build();

        JobWrapper<String, Person> JobWrapper = new JobWrapper.Builder<String, Person>()
                .job((String object, Map<String, JobWrapper> allWrappers) -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return new Person("Person0");
                })
                .param("0")
                .id("first")
                .next(JobWrapper1, true)
                .callback((boolean success, String param, JobResult<Person> JobResult) ->
                        System.out.println(String.format("thread is %s, param is %s, result is %s", Thread.currentThread().getName(), param, JobResult)))
                .build();

        //虽然尚未执行，但是也可以先取得结果的引用，作为下一个任务的入参。V1.2前写法，需要手工给
        //V1.3后，不用给JobWrapper setParam了，直接在job的action里自行根据id获取即可.参考dependnew包下代码
        JobResult<Person> result = JobWrapper.getJobResult();
        JobResult<Person> result1 = JobWrapper1.getJobResult();
        JobWrapper1.setParam(result);
        JobWrapper2.setParam(result1);

        MultiJobsAsynSolution.beginJob(3500, JobWrapper);

        System.out.println(JobWrapper2.getJobResult());
        MultiJobsAsynSolution.shutDown();
    }
}
