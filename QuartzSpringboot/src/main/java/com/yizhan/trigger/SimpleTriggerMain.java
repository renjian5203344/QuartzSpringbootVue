package com.yizhan.trigger;



import com.yizhan.listener.SimpleJobListener;
import com.yizhan.listener.SimpleTriggerListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.EverythingMatcher;
import org.quartz.impl.matchers.KeyMatcher;


import java.util.Date;

public class SimpleTriggerMain {
    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();//获取一个调度工厂
        Scheduler scheduler = schedulerFactory.getScheduler();//获取一个调度器
        //建造者模式
        JobDetail job = JobBuilder.newJob(Myjob.class).withIdentity("job1","group1").build();
        //在当前15S后运行
        Date startTime = DateBuilder.nextGivenSecondDate(new Date(),5);
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1")
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2)
                        .withRepeatCount(5)).build();



        //1. 注册全局监听器,所有的任务都是由监听器去监听，只要有事件它都会打印出消息
        //scheduler.getListenerManager().addJobListener(new SimpleJobListener(), EverythingMatcher.allJobs());

        //2.注册局部监听器
        scheduler.getListenerManager().addJobListener(new SimpleJobListener(), KeyMatcher.keyEquals(JobKey.jobKey("job1", "group1")));

        //3.创建并注册一个全局的Trigger Listener
       // scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener("SimpleTrigger"), EverythingMatcher.allTriggers());//匹配所有Triggers EverythingMatcher.allTriggers()

        //4.创建并注册一个局部的Trigger Listener
         scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener("SimpleTrigger"), KeyMatcher.keyEquals(TriggerKey.triggerKey("trigger1", "group1")));


        // 添加到调度器中
        scheduler.scheduleJob(job,trigger);
        scheduler.start();

    }

}
