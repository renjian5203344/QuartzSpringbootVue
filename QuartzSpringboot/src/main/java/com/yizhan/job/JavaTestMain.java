package com.yizhan.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public class JavaTestMain {
    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobDataMap newJobDataMap = new JobDataMap();

        newJobDataMap.put("jarPath" ,"/Users/newsan/yizhanJavaTask2.jar");
        newJobDataMap.put("parameter","test1\t\ttest2");
        newJobDataMap.put("vmParam","-Xms512m\t\t-Xmx512m\t\t-Xmn200m");
        //建造者模式
        JobDetail job = JobBuilder.newJob(JavaTask.class).withIdentity("job1","group1").usingJobData(newJobDataMap).build();
        //在当前15S后运行
        Date startTime = DateBuilder.nextGivenSecondDate(new Date(),5);
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1")
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2)
                        .withRepeatCount(5)).build();



        // 添加到调度器中
        scheduler.scheduleJob(job,trigger);
        scheduler.start();

    }

}
