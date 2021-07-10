package multijobs.seq;


import java.util.concurrent.ExecutionException;

import com.bob.boot.thread.CurrentTimeUtil;
import com.bob.boot.thread.multijobs.job.JobWrapper;
import com.bob.boot.thread.multijobs.job.processor.MultiJobsAsynSolution;

/**
 * 串行测试
 * @author bob
 */
@SuppressWarnings("Duplicates")
public class TestSeqTimeout {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        testFirstTimeout();
    }

    /**
     * begin-1576719450476
     * callback job0 failure--1576719451338----job0--default-threadName:main
     * callback job1 failure--1576719451338----job1--default-threadName:main
     * callback job2 failure--1576719451338----job2--default-threadName:main
     * end-1576719451338
     * cost-862
     */
    private static void testFirstTimeout() throws ExecutionException, InterruptedException {
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();
        TimeoutJob t = new TimeoutJob();

        JobWrapper<String, String> jobWrapper2 = new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .build();

        JobWrapper<String, String> jobWrapper1 = new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .next(jobWrapper2)
                .build();

        //2在1后面串行
        //T会超时
        JobWrapper<String, String> jobWrapperT = new JobWrapper.Builder<String, String>()
                .job(t)
                .callback(t)
                .param("t")
                .next(jobWrapper1)
                .build();


        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        MultiJobsAsynSolution.beginJob(5000, jobWrapperT);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        MultiJobsAsynSolution.shutDown();
    }

}
