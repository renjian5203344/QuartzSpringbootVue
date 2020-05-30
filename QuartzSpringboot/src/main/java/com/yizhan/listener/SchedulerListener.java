package com.yizhan.listener;
import org.quartz.*;
public class SchedulerListener implements org.quartz.SchedulerListener {
    @Override
    public void jobScheduled(Trigger trigger) {
        String triggerName = trigger.getKey().getName();
        System.out.println(triggerName + " was jobScheduled==jobScheduled");
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        String triggerName = triggerKey.getName();
        System.out.println(triggerName + " was jobUnscheduled==jobUnscheduled");
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        String triggerName = trigger.getKey().getName();
        System.out.println(triggerName + " was triggerFinalized==triggerFinalized");
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        String triggerName = triggerKey.getName();
        System.out.println(triggerName + " was triggerPaused==triggerPaused");
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        System.out.println(triggerGroup + " was triggersPaused==triggersPaused");
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        String triggerName = triggerKey.getName();
        System.out.println(triggerName + " was triggerResumed==triggerResumed");
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        System.out.println(triggerGroup + " was triggersResumed==triggersResumed");
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        String jobname = jobDetail.getKey().getName();
        System.out.println(jobname + " was jobAdded==jobAdded");
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        String jobname = jobKey.getName();
        System.out.println(jobname + " was jobDeleted==jobDeleted");
    }

    @Override
    public void jobPaused(JobKey jobKey) {
        String jobname = jobKey.getName();
        System.out.println(jobname + " was jobPaused==jobPaused");
    }

    @Override
    public void jobsPaused(String jobGroup) {
        System.out.println(jobGroup + " was jobPaused==jobPaused");
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        String jobname = jobKey.getName();
        System.out.println(jobname + " was jobResumed==jobResumed");
    }

    @Override
    public void jobsResumed(String jobGroup) {
        System.out.println(jobGroup + " was jobResumed==jobResumed");
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        System.out.println(msg + " was schedulerError==schedulerError");
    }

    @Override
    public void schedulerInStandbyMode() {
        System.out.println(" schedulerInStandbyMode==schedulerInStandbyMode");
    }

    @Override
    public void schedulerStarted() {
        System.out.println(" schedulerStarted==schedulerStarted");
    }

    @Override
    public void schedulerStarting() {
        System.out.println(" schedulerStarting==schedulerStarting");
    }

    @Override
    public void schedulerShutdown() {
        System.out.println(" schedulerShutdown==schedulerShutdown");
    }

    @Override
    public void schedulerShuttingdown() {
        System.out.println(" schedulerShuttingdown==schedulerShuttingdown");
    }

    @Override
    public void schedulingDataCleared() {
        System.out.println(" schedulingDataCleared==schedulingDataCleared");
    }
}