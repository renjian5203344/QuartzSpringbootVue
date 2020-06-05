package com.yizhan.controller;

import com.yizhan.trigger.Myjob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class QuartzJobCotroller {

    @Autowired
    private Scheduler scheduler;

    @RequestMapping(value ="startjob", method = RequestMethod.GET)
    public void startjob(){
        //建造者模式
        JobDetail job = JobBuilder.newJob(Myjob.class).withIdentity("job1","group1").build();
        //在当前15S后运行
        Date startTime = DateBuilder.nextGivenSecondDate(new Date(),5);
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1")
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2)
                        .withRepeatCount(5)).build();

        try {
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}

