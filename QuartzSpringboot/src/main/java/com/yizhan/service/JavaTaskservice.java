package com.yizhan.service;

import com.sun.tools.classfile.ConstantPool;
import com.yizhan.dataobject.JavaQuartz;
import com.yizhan.enums.JobStatusEnum;
import com.yizhan.job.JavaTask;
import com.yizhan.repository.JavaQuartzTaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JavaTaskservice {

    @Autowired
    JavaQuartzTaskRepository repository;

    @Autowired
    private Scheduler scheduler;

    public void saveJavaTask(JavaQuartz javaQuartz) {
      if (javaQuartz.getParentTaskId()==0L){
          javaQuartz.setParentTaskId(-1L);

      }

        javaQuartz.setJobStatus(JobStatusEnum.NEW.getCode());
        repository.save(javaQuartz);
        JobDataMap newJobDataMap = getJobDataMap(javaQuartz);
        JobDetail job = JobBuilder.newJob(JavaTask.class).withIdentity(javaQuartz.getJobName(), javaQuartz.getJobGroup()).usingJobData(newJobDataMap).build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(javaQuartz.getJobName(), javaQuartz.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(javaQuartz.getCronExpression()))
                .build();
        try {
            scheduler.scheduleJob(job,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }


    }


    public void pauseJavaTask(JavaQuartz javaQuartz) {

//        Optional<JavaQuartz> optional = repository.findById(javaQuartz.getId());
//        JavaQuartz javaQuartz1 = optional.get();
        JavaQuartz javaQuartz1 = repository.getOne(javaQuartz.getId());

        if (javaQuartz1!=null){

//            repository.deleteById(javaQuartz.getId());
            javaQuartz1.setJobStatus(JobStatusEnum.PAUSE.getCode());
//            repository.save(javaQuartz1);

            repository.flush();
        }

        JobKey jobKey = new JobKey(javaQuartz.getJobName(),javaQuartz.getJobGroup());
        try {
            scheduler.pauseJob(jobKey);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }


    }


    public void resumeJavaTask(JavaQuartz javaQuartz) {

//        Optional<JavaQuartz> optional = repository.findById(javaQuartz.getId());
//        JavaQuartz javaQuartz1 = optional.get();
        JavaQuartz javaQuartz1 = repository.getOne(javaQuartz.getId());

        if (javaQuartz1!=null){

            javaQuartz1.setJobStatus(JobStatusEnum.RESUME.getCode());

            repository.flush();
        }

        JobKey jobKey = new JobKey(javaQuartz.getJobName(),javaQuartz.getJobGroup());
        try {
            scheduler.resumeJob(jobKey);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }


    }



    /***
     * 构建JobDataMap
     * @param javaQuartz
     * @return  JobDataMap对象
     */


    private JobDataMap getJobDataMap(JavaQuartz javaQuartz){
        JobDataMap jobDataMap = new JobDataMap();
        String jarPath = javaQuartz.getJarPath();
        String parameter = javaQuartz.getParameter();
        String vmParam = javaQuartz.getVmParam();

        jobDataMap.put("name",javaQuartz.getJobName());
        jobDataMap.put("group",javaQuartz.getJobGroup());
        if (StringUtils.isNotBlank(jarPath)){
            jobDataMap.put("jarPath" ,jarPath);

        }
        if (StringUtils.isNotBlank(parameter)){
            jobDataMap.put("parameter",parameter);

        }
        if (StringUtils.isNotBlank(vmParam)){
            jobDataMap.put("vmParam",vmParam);

        }

        return jobDataMap;
//
//


    }





}
