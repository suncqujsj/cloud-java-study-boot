package com.java.study.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class Test {
	
	public static void main(String[] args) {
		try
		{
			//定时器对象
			 Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			 //定义一个工作对象 设置工作名称与组名
			 JobDetail job =JobBuilder.newJob(HelloJob.class).withIdentity("job1","group1").build();		 
		     //定义一个触发器 简单Trigger 设置工作名称与组名 5秒触发一次
             //Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1").startNow().
	         //withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5)).build();
			 //定义一个任务调度的Trigger 设置工作名称与组名 每天的24:00触发一次
			 Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1").withSchedule
			 //(CronScheduleBuilder.cronSchedule("0 0 0 * * ?")).build();
			 //0 10 20 30 40 50秒的时候执行一次，其他条件不限制
			 (CronScheduleBuilder.cronSchedule("0/10/20/30/40/50 * * * * ?")).build();
			 //设置工作 与触发器
		     scheduler.scheduleJob(job, trigger);
			 // and start it off
			 //开始定时任务
		     scheduler.start();
		}catch(Exception e)
		{
			
		}
	}
}