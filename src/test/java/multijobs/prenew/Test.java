package multijobs.prenew;

import java.util.concurrent.ExecutionException;

import com.bob.boot.entity.Person;
import com.bob.boot.thread.multijobs.job.JobWrapper;
import com.bob.boot.thread.multijobs.job.processor.MultiJobsAsynSolution;

/**
 * 后面请求依赖于前面请求的执行结果
 * @author bob
 * @version 1.0
 */
public class Test {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Job0 j = new Job0();
        Job1 j1 = new Job1();
        Job2 j2 = new Job2();

        JobWrapper<Person, String> jobWrapper2 =  new JobWrapper.Builder<Person, String>()
                .job(j2)
                .callback(j2)
                .id("third")
                .build();

        JobWrapper<String, Person> jobWrapper1 = new JobWrapper.Builder<String, Person>()
                .job(j1)
                .callback(j1)
                .id("second")
                .next(jobWrapper2)
                .build();

        JobWrapper<String, Person> jobWrapper = new JobWrapper.Builder<String, Person>()
                .job(j)
                .param("0")
                .id("first")
                .next(jobWrapper1)
                .callback(j)
                .build();

        //V1.3后，不用给jobWrapper setParam了，直接在job的action里自行根据id获取即可

        MultiJobsAsynSolution.beginJob(3500, jobWrapper);

        System.out.println(jobWrapper2.getJobResult());
        MultiJobsAsynSolution.shutDown();
    }
}
