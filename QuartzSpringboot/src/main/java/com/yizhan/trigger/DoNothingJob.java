package com.yizhan.trigger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class DoNothingJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("do nothing");

    }
}
