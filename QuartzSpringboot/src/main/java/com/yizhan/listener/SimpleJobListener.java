package com.yizhan.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class SimpleJobListener implements JobListener {
    @Override
    public String getName() {
        String name = getClass().getSimpleName();
        System.out.println(" listener name is:"+name);
        return name;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        String jobName = context.getJobDetail().getKey().getName();
        System.out.println(jobName + " is going to be executed==by methdod ==jobToBeExecuted");
    }



    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        String jobName = context.getJobDetail().getKey().getName();
        System.out.println(jobName + " is going to be executed==by methdod ==jobExecutionVetoed");

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        String jobName = context.getJobDetail().getKey().getName();
        System.out.println(jobName + " is going to be executed==by methdod ==jobWasExecuted");
    }
}
