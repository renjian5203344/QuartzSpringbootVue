package com.yizhan.trigger;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


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
        // 添加到调度器中
        scheduler.scheduleJob(job,trigger);
        scheduler.start();

    }

}
