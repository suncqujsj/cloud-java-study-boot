package multijobs.pre;

import java.util.concurrent.ExecutionException;

import com.bob.boot.entity.Person;
import com.bob.boot.thread.multijobs.job.JobResult;
import com.bob.boot.thread.multijobs.job.JobWrapper;
import com.bob.boot.thread.multijobs.job.processor.MultiJobsAsynSolution;


/**
 * 后面请求依赖于前面请求的执行结果
 * @author bob
 * @version 1.0
 */
public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Job w = new Job();
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();

        JobWrapper<JobResult<Person>, String> JobWrapper2 =  new JobWrapper.Builder<JobResult<Person>, String>()
                .job(w2)
                .callback(w2)
                .id("third")
                .build();

        JobWrapper<JobResult<Person>, Person> JobWrapper1 = new JobWrapper.Builder<JobResult<Person>, Person>()
                .job(w1)
                .callback(w1)
                .id("second")
                .next(JobWrapper2)
                .build();

        JobWrapper<String, Person> JobWrapper = new JobWrapper.Builder<String, Person>()
                .job(w)
                .param("0")
                .id("first")
                .next(JobWrapper1, true)
                .callback(w)
                .build();

        //虽然尚未执行，但是也可以先取得结果的引用，作为下一个任务的入参。V1.2前写法，需要手工给
        //V1.3后，不用给JobWrapper setParam了，直接在job的action里自行根据id获取即可.参考preNew包下代码
        JobResult<Person> result = JobWrapper.getJobResult();
        JobResult<Person> result1 = JobWrapper1.getJobResult();
        JobWrapper1.setParam(result);
        JobWrapper2.setParam(result1);

        MultiJobsAsynSolution.beginJob(3500, JobWrapper);

        System.out.println(JobWrapper2.getJobResult());
        MultiJobsAsynSolution.shutDown();
    }
}
