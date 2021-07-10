package multijobs.seq;


import java.util.concurrent.ExecutionException;

import com.bob.boot.thread.CurrentTimeUtil;
import com.bob.boot.thread.multijobs.job.JobWrapper;
import com.bob.boot.thread.multijobs.job.processor.MultiJobsAsynSolution;

/**
 * 串行测试
 * @author bob
 */
public class TestSeq {
    public static void main(String[] args) throws InterruptedException, ExecutionException {


        Job0 w = new Job0();
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();

        //顺序0-1-2
        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .next(jobWrapper2)
                .build();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .next(jobWrapper1)
                .build();

//        testNormal(jobWrapper);

        testGroupTimeout(jobWrapper);
    }

    private static void testNormal(JobWrapper<String, String> jobWrapper) throws ExecutionException, InterruptedException {
        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        MultiJobsAsynSolution.beginJob(3500, jobWrapper);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        MultiJobsAsynSolution.shutDown();
    }

    private static void testGroupTimeout(JobWrapper<String, String> jobWrapper) throws ExecutionException, InterruptedException {
        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        MultiJobsAsynSolution.beginJob(2500, jobWrapper);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        MultiJobsAsynSolution.shutDown();
    }
}
