package multijobs.parallel;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import com.bob.boot.thread.CurrentTimeUtil;
import com.bob.boot.thread.multijobs.job.JobWrapper;
import com.bob.boot.thread.multijobs.job.processor.MultiJobsAsynSolution;

/**
 * 并行测试
 *
 * @author bob
 */
@SuppressWarnings("ALL")
public class TestJob {
    public static void main(String[] args) throws Exception {

//        testNormal();
//        testMulti();
//        testMultiReverse();
//        testMultiError2();
//        testMulti3();
//        testMulti3Reverse();
//        testMulti4();
//        testMulti4Reverse();
//        testMulti5();
//        testMulti5Reverse();
//        testMulti6();
//        testMulti7();
//        testMulti8();
//        testMulti9();
        testMulti9Reverse();
    }

    /**
     * 3个并行，测试不同时间的超时
     */
    private static void testNormal() throws InterruptedException, ExecutionException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .build();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .build();

        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        MultiJobsAsynSolution.beginJob(1500, jobWrapper, jobWrapper1, jobWrapper2);
//        MultiJobsAsynSolution.beginJob(800, jobWrapper, jobWrapper1, jobWrapper2);
//        MultiJobsAsynSolution.beginJob(1000, jobWrapper, jobWrapper1, jobWrapper2);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));
        System.out.println(MultiJobsAsynSolution.getThreadCount());

        System.out.println(jobWrapper.getJobResult());
        MultiJobsAsynSolution.shutDown();
    }

    /**
     * 0,2同时开启,1在0后面
     * 0---1
     * 2
     */
    private static void testMulti() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .build();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .next(jobWrapper1)
                .build();

        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        MultiJobsAsynSolution.beginJob(2500, jobWrapper, jobWrapper2);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        MultiJobsAsynSolution.shutDown();
    }

    /**
     * 0,2同时开启,1在0后面
     * 0---1
     * 2
     */
    private static void testMultiReverse() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .pre(jobWrapper)
                .build();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .build();


        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        MultiJobsAsynSolution.beginJob(2500, jobWrapper, jobWrapper2);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        MultiJobsAsynSolution.shutDown();
    }


    /**
     * 0,2同时开启,1在0后面. 组超时,则0和2成功,1失败
     * 0---1
     * 2
     */
    private static void testMultiError() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .build();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .next(jobWrapper1)
                .build();

        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        MultiJobsAsynSolution.beginJob(1500, jobWrapper, jobWrapper2);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        MultiJobsAsynSolution.shutDown();
    }

    /**
     * 0执行完,同时1和2, 1\2都完成后3
     *     1
     * 0       3
     *     2
     */
    private static void testMulti3() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();
        Job3 w3 = new Job3();

        JobWrapper<String, String> jobWrapper3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("3")
                .build();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .next(jobWrapper3)
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .next(jobWrapper3)
                .build();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .next(jobWrapper1, jobWrapper2)
                .build();


        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        MultiJobsAsynSolution.beginJob(3100, jobWrapper);
//        MultiJobsAsynSolution.beginJob(2100, jobWrapper);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        System.out.println(MultiJobsAsynSolution.getThreadCount());
        MultiJobsAsynSolution.shutDown();
    }

    /**
     * 0执行完,同时1和2, 1\2都完成后3
     *     1
     * 0       3
     *     2
     */
    private static void testMulti3Reverse() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();
        Job3 w3 = new Job3();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .build();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .pre(jobWrapper)
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .pre(jobWrapper)
                .build();

        JobWrapper<String, String> jobWrapper3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("3")
                .pre(jobWrapper1, jobWrapper2)
                .build();


        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        MultiJobsAsynSolution.beginJob(3100, jobWrapper);
//        MultiJobsAsynSolution.beginJob(2100, jobWrapper);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        System.out.println(MultiJobsAsynSolution.getThreadCount());
        MultiJobsAsynSolution.shutDown();
    }


    /**
     * 0执行完,同时1和2, 1\2都完成后3，2耗时2秒，1耗时1秒。3会等待2完成
     *     1
     * 0       3
     *     2
     *
     * 执行结果0，1，2，3
     */
    private static void testMulti4() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();

        Job2 w2 = new Job2();
        w2.setSleepTime(2000);

        Job3 w3 = new Job3();

        JobWrapper<String, String> jobWrapper3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("3")
                .build();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .next(jobWrapper3)
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .next(jobWrapper3)
                .build();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .next(jobWrapper1, jobWrapper2)
                .build();

        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        //正常完毕
        MultiJobsAsynSolution.beginJob(4100, jobWrapper);
        //3会超时
//        MultiJobsAsynSolution.beginJob(3100, jobWrapper);
        //2,3会超时
//        MultiJobsAsynSolution.beginJob(2900, jobWrapper);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        System.out.println(MultiJobsAsynSolution.getThreadCount());
        MultiJobsAsynSolution.shutDown();
    }

    /**
     * 0执行完,同时1和2, 1\2都完成后3，2耗时2秒，1耗时1秒。3会等待2完成
     *     1
     * 0       3
     *     2
     *
     * 执行结果0，1，2，3
     */
    private static void testMulti4Reverse() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();

        Job2 w2 = new Job2();
        w2.setSleepTime(2000);

        Job3 w3 = new Job3();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .build();

        JobWrapper<String, String> jobWrapper3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("3")
                .build();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .pre(jobWrapper)
                .next(jobWrapper3)
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .pre(jobWrapper)
                .next(jobWrapper3)
                .build();

        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        //正常完毕
        MultiJobsAsynSolution.beginJob(4100, jobWrapper);
        //3会超时
//        MultiJobsAsynSolution.beginJob(3100, jobWrapper);
        //2,3会超时
//        MultiJobsAsynSolution.beginJob(2900, jobWrapper);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        System.out.println(MultiJobsAsynSolution.getThreadCount());
        MultiJobsAsynSolution.shutDown();
    }

    /**
     * 0执行完,同时1和2, 1\2 任何一个执行完后，都执行3
     *     1
     * 0       3
     *     2
     *
     * 则结果是：
     * 0，2，3，1
     * 2，3分别是500、400.3执行完毕后，1才执行完
     */
    private static void testMulti5() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();

        Job2 w2 = new Job2();
        w2.setSleepTime(500);

        Job3 w3 = new Job3();
        w3.setSleepTime(400);

        JobWrapper<String, String> jobWrapper3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("3")
                .build();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .next(jobWrapper3, false)
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .next(jobWrapper3, false)
                .build();

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .next(jobWrapper1, jobWrapper2)
                .build();

        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        //正常完毕
        MultiJobsAsynSolution.beginJob(4100, jobWrapper);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        System.out.println(MultiJobsAsynSolution.getThreadCount());
        MultiJobsAsynSolution.shutDown();
    }


    /**
     * 0执行完,同时1和2, 1\2 任何一个执行完后，都执行3
     *     1
     * 0       3
     *     2
     *
     * 则结果是：
     * 0，2，3，1
     * 2，3分别是500、400.3执行完毕后，1才执行完
     */
    private static void testMulti5Reverse() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();

        Job2 w2 = new Job2();
        w2.setSleepTime(500);

        Job3 w3 = new Job3();
        w3.setSleepTime(400);

        JobWrapper<String, String> jobWrapper =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .build();

        JobWrapper<String, String> jobWrapper3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("3")
                .build();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .pre(jobWrapper, true)
                .next(jobWrapper3, false)
                .build();

        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .pre(jobWrapper, true)
                .next(jobWrapper3, false)
                .build();



        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        //正常完毕
        MultiJobsAsynSolution.beginJob(4100, jobWrapper);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        System.out.println(MultiJobsAsynSolution.getThreadCount());
        MultiJobsAsynSolution.shutDown();
    }

    /**
     * 0执行完,同时1和2, 必须1执行完毕后，才能执行3. 无论2是否领先1完毕，都要等1
     *     1
     * 0       3
     *     2
     *
     * 则结果是：
     * 0，2，1，3
     *
     * 2，3分别是500、400.2执行完了，1没完，那就等着1完毕，才能3
     */
    private static void testMulti6() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();

        Job2 w2 = new Job2();
        w2.setSleepTime(500);

        Job3 w3 = new Job3();
        w3.setSleepTime(400);

        JobWrapper<String, String> jobWrapper3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("3")
                .build();

        //设置2不是必须
        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .next(jobWrapper3, false)
                .build();
        // 设置1是必须的
        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .next(jobWrapper3, true)
                .build();

        JobWrapper<String, String> jobWrapper0 =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .next(jobWrapper2, jobWrapper1)
                .build();


        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        //正常完毕
        MultiJobsAsynSolution.beginJob(4100, jobWrapper0);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        System.out.println(MultiJobsAsynSolution.getThreadCount());
        MultiJobsAsynSolution.shutDown();
    }

    /**
     * 两个0并行，上面0执行完,同时1和2, 下面0执行完开始1，上面的 必须1、2执行完毕后，才能执行3. 最后必须2、3都完成，才能4
     *     1
     * 0       3
     *     2        4
     * ---------
     * 0   1   2
     *
     * 则结果是：
     * callback job0 success--1577242870969----result = 1577242870968---param = 00 from 0-threadName:Thread-1
     * callback job0 success--1577242870969----result = 1577242870968---param = 0 from 0-threadName:Thread-0
     * callback job1 success--1577242871972----result = 1577242871972---param = 11 from 1-threadName:Thread-1
     * callback job1 success--1577242871972----result = 1577242871972---param = 1 from 1-threadName:Thread-2
     * callback job2 success--1577242871973----result = 1577242871973---param = 2 from 2-threadName:Thread-3
     * callback job2 success--1577242872975----result = 1577242872975---param = 22 from 2-threadName:Thread-1
     * callback job3 success--1577242872977----result = 1577242872977---param = 3 from 3-threadName:Thread-2
     * callback job4 success--1577242873980----result = 1577242873980---param = 4 from 3-threadName:Thread-2
     */
    private static void testMulti7() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();
        Job2 w2 = new Job2();
        Job3 w3 = new Job3();
        Job4 w4 = new Job4();

        JobWrapper<String, String> jobWrapper4 =  new JobWrapper.Builder<String, String>()
                .job(w4)
                .callback(w4)
                .param("4")
                .build();

        JobWrapper<String, String> jobWrapper3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("3")
                .next(jobWrapper4)
                .build();

        //下面的2
        JobWrapper<String, String> jobWrapper22 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("22")
                .next(jobWrapper4)
                .build();

        //下面的1
        JobWrapper<String, String> jobWrapper11 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("11")
                .next(jobWrapper22)
                .build();

        //下面的0
        JobWrapper<String, String> jobWrapper00 =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("00")
                .next(jobWrapper11)
                .build();

        //上面的1
        JobWrapper<String, String> jobWrapper1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("1")
                .next(jobWrapper3)
                .build();

        //上面的2
        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("2")
                .next(jobWrapper3)
                .build();

        //上面的0
        JobWrapper<String, String> jobWrapper0 =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("0")
                .next(jobWrapper1, jobWrapper2)
                .build();

        long now = CurrentTimeUtil.currentTimeMillis();
        System.out.println("begin-" + now);

        //正常完毕
        MultiJobsAsynSolution.beginJob(4100, jobWrapper00, jobWrapper0);

        System.out.println("end-" + CurrentTimeUtil.currentTimeMillis());
        System.err.println("cost-" + (CurrentTimeUtil.currentTimeMillis() - now));

        System.out.println(MultiJobsAsynSolution.getThreadCount());
        MultiJobsAsynSolution.shutDown();
    }

    /**
     * a1 -> b -> c
     * a2 -> b -> c
     *
     * b、c
     */
    private static void testMulti8() throws ExecutionException, InterruptedException {
        Job0 w = new Job0();
        Job1 w1 = new Job1();
        w1.setSleepTime(1005);

        Job2 w2 = new Job2();
        w2.setSleepTime(3000);
        Job3 w3 = new Job3();
        w3.setSleepTime(1000);

        JobWrapper<String, String> jobWrapper3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("c")
                .build();

        JobWrapper<String, String> jobWrapper2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("b")
                .next(jobWrapper3)
                .build();

        JobWrapper<String, String> jobWrappera1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("a1")
                .next(jobWrapper2)
                .build();
        JobWrapper<String, String> jobWrappera2 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("a2")
                .next(jobWrapper2)
                .build();


        MultiJobsAsynSolution.beginJob(6000, jobWrappera1, jobWrappera2);
        MultiJobsAsynSolution.shutDown();
    }

    /**
     * w1 -> w2 -> w3
     *            ---  last
     * w
     * w1和w并行，w执行完后就执行last，此时b、c还没开始，b、c就不需要执行了
     */
    private static void testMulti9() throws ExecutionException, InterruptedException {
        Job1 w1 = new Job1();
        //注意这里，如果w1的执行时间比w长，那么w2和w3肯定不走。 如果w1和w执行时间一样长，多运行几次，会发现w2有时走有时不走
//        w1.setSleepTime(1100);

        Job0 w = new Job0();
        Job2 w2 = new Job2();
        Job3 w3 = new Job3();
        Job4 w4 = new Job4();

        JobWrapper<String, String> last =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("last")
                .build();

        JobWrapper<String, String> wrapperW =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("w")
                .next(last, false)
                .build();

        JobWrapper<String, String> wrapperW3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("w3")
                .next(last, false)
                .build();

        JobWrapper<String, String> wrapperW2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("w2")
                .next(wrapperW3)
                .build();

        JobWrapper<String, String> wrapperW1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("w1")
                .next(wrapperW2)
                .build();

        MultiJobsAsynSolution.beginJob(6000, wrapperW, wrapperW1);
        MultiJobsAsynSolution.shutDown();
    }

    /**
     * w1 -> w2 -> w3
     *            ---  last
     * w
     * w1和w并行，w执行完后就执行last，此时b、c还没开始，b、c就不需要执行了
     */
    private static void testMulti9Reverse() throws ExecutionException, InterruptedException {
        Job1 w1 = new Job1();
        //注意这里，如果w1的执行时间比w长，那么w2和w3肯定不走。 如果w1和w执行时间一样长，多运行几次，会发现w2有时走有时不走
//        w1.setSleepTime(1100);

        Job0 w = new Job0();
        Job2 w2 = new Job2();
        Job3 w3 = new Job3();
        Job4 w4 = new Job4();

        JobWrapper<String, String> wrapperW1 =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("w1")
                .build();

        JobWrapper<String, String> wrapperW =  new JobWrapper.Builder<String, String>()
                .job(w)
                .callback(w)
                .param("w")
                .build();

        JobWrapper<String, String> last =  new JobWrapper.Builder<String, String>()
                .job(w1)
                .callback(w1)
                .param("last")
                .pre(wrapperW)
                .build();

        JobWrapper<String, String> wrapperW2 =  new JobWrapper.Builder<String, String>()
                .job(w2)
                .callback(w2)
                .param("w2")
                .pre(wrapperW1)
                .build();

        JobWrapper<String, String> wrapperW3 =  new JobWrapper.Builder<String, String>()
                .job(w3)
                .callback(w3)
                .param("w3")
                .pre(wrapperW2)
                .next(last, false)
                .build();

        MultiJobsAsynSolution.beginJob(6000,Executors.newCachedThreadPool(),  wrapperW, wrapperW1);
        MultiJobsAsynSolution.shutDown();
    }
}