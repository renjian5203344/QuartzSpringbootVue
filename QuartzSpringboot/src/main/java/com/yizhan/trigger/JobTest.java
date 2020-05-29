package com.yizhan.trigger;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class JobTest {

    public static void main(String[] args) throws SchedulerException, IOException {
        JobDetail job=newJob()
                .ofType(Myjob.class) //引用Job Class
                .withIdentity("job1", "group1") //设置name/group
                .withDescription("this is a test job") //设置描述
                .usingJobData("age", 18) //加入属性到ageJobDataMap
                .build();

        job.getJobDataMap().put("name", "quertz"); //加入属性name到JobDataMap

        //定义一个每秒执行一次的SimpleTrigger
        Trigger trigger=newTrigger()
                .startNow()
                .withIdentity("trigger1")
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(1)
                        .repeatForever())
                .build();

        Scheduler sche= StdSchedulerFactory.getDefaultScheduler();
        sche.scheduleJob(job, trigger);

        sche.start();

        System.in.read();

        sche.shutdown();
    }

}
