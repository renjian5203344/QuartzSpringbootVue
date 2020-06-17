package com.yizhan.service;

import com.yizhan.dataobject.JavaQuartz;
import com.yizhan.enums.JobStatusEnum;
import com.yizhan.job.JavaTask;
import com.yizhan.listener.JavaTaskListener;
import com.yizhan.listener.SimpleJobListener;
import com.yizhan.repository.JavaQuartzTaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JavaTaskService {

    @Autowired
    JavaQuartzTaskRepository repository;

    @Autowired
    private Scheduler scheduler;

    public void saveJavaTask(JavaQuartz javaQuartz) {//入参JavaQuartz实体
        if (StringUtils.isBlank(javaQuartz.getParentTaskId())) {
            javaQuartz.setParentTaskId("-1");

        }

        JavaQuartz result =repository.findByJobNameAndJobGroup(javaQuartz.getJobName(),javaQuartz.getJobGroup());
        if (result != null) {
            System.out.println("jobName 和　jobGroup已经存在");
            return;

        }
        javaQuartz.setJobStatus(JobStatusEnum.NEW.getCode());
        repository.save(javaQuartz);
        JobDataMap newJobDataMap = getJobDataMap(javaQuartz);//构建数据对象newJobDataMap
        //构建JobDetai
        JobDetail job =
                JobBuilder.newJob(JavaTask.class).withIdentity(javaQuartz.getJobName(), javaQuartz.getJobGroup()).usingJobData(newJobDataMap).build();
        //构建Trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(javaQuartz.getJobName(), javaQuartz.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(javaQuartz.getCronExpression()))
                .build();
        try {
            //注册监听器
          //  scheduler.getListenerManager().addJobListener(new SimpleJobListener(), KeyMatcher.keyEquals(JobKey.jobKey("job1", "group1")));
            scheduler.getListenerManager().addJobListener(new JavaTaskListener(repository,scheduler), KeyMatcher.keyEquals(JobKey.jobKey(javaQuartz.getJobName(), javaQuartz.getJobGroup())));

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }


    public void pauseJavaTask(JavaQuartz javaQuartz) {

//        Optional<JavaQuartz> optional = repository.findById(javaQuartz.getId());
//        JavaQuartz javaQuartz1 = optional.get();
        JavaQuartz javaQuartz1 = repository.getOne(javaQuartz.getId());

        if (javaQuartz1 != null) {

//            repository.deleteById(javaQuartz.getId());
            javaQuartz1.setJobStatus(JobStatusEnum.PAUSE.getCode());
//            repository.save(javaQuartz1);

            repository.flush();
        }

        JobKey jobKey = new JobKey(javaQuartz.getJobName(), javaQuartz.getJobGroup());
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

        if (javaQuartz1 != null) {

            javaQuartz1.setJobStatus(JobStatusEnum.RESUME.getCode());

            repository.flush();
        }

        JobKey jobKey = new JobKey(javaQuartz.getJobName(), javaQuartz.getJobGroup());
        try {
            scheduler.resumeJob(jobKey);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }


    }


    /***
     * 构建JobDataMap对象
     * @param javaQuartz  Java任务实体类
     * @return JobDataMap对象
     */


    public static JobDataMap getJobDataMap(JavaQuartz javaQuartz) {
        JobDataMap jobDataMap = new JobDataMap();
        String jarPath = javaQuartz.getJarPath();//取得jarPath
        String parameter = javaQuartz.getParameter();//取得 parameter
        String vmParam = javaQuartz.getVmParam();//取得 vmParam

        jobDataMap.put("name", javaQuartz.getJobName());
        jobDataMap.put("group", javaQuartz.getJobGroup());
        if (StringUtils.isNotBlank(jarPath)) {//判断jarPath不为空
            jobDataMap.put("jarPath", jarPath);

        }
        if (StringUtils.isNotBlank(parameter)) {
            jobDataMap.put("parameter", parameter);

        }
        if (StringUtils.isNotBlank(vmParam)) {
            jobDataMap.put("vmParam", vmParam);

        }

        return jobDataMap;
//
//


    }


    public void createTuoPuJavaTask(JavaQuartz javaQuartz) {

        javaQuartz.setJobStatus(JobStatusEnum.ENABLED.getCode());

        if (StringUtils.isBlank(javaQuartz.getParentTaskId())) {
            javaQuartz.setParentTaskId("-1");

        }


        JavaQuartz javaQuartz11 =repository.findByJobNameAndJobGroup(javaQuartz.getJobName(),javaQuartz.getJobGroup());
        if (javaQuartz11 !=null) {
            System.out.println("jobName 和　jobGroup已经存在");
            return;

        }


      repository.save(javaQuartz);
        JavaQuartz javaquartz33 = repository.findByJobNameAndJobGroup(javaQuartz.getJobName(),javaQuartz.getJobGroup());
        String quartzEntityId = javaquartz33.getId()+"";
        String parentTaskId = javaQuartz.getParentTaskId();
        if(!parentTaskId.equals("-1")){
            String[] parentids = parentTaskId.split(",");
            for(String id:parentids){
                if(StringUtils.isNotBlank(id)){
                    JavaQuartz javaQuartz22 = repository.findById(Long.valueOf(id)).get();
                    String preChild = javaQuartz22.getChildTaskId();
                    String [] prechildString = preChild.split(",");
                    boolean isAdd = true;
                    for(String prechildid:prechildString){
                        if(prechildid.equals(quartzEntityId)){
                            isAdd = false;
                            break;
                        }
                    }
                    if(isAdd){
                        preChild += preChild + ","+quartzEntityId;
                    }
                    javaQuartz22.setChildTaskId(preChild);
                    repository.save(javaQuartz22);
                }
            }
        }
    }


    /**
     * 启动单个拓扑任务方法
     * @param id
     */
    public void startSingleTuoPuJob(Long id) {

        JavaQuartz javaQuartz = repository.getOne(id);

        if (javaQuartz != null && javaQuartz.getJobStatus()==JobStatusEnum.ENABLED.getCode()&&"-1".equals(javaQuartz.getParentTaskId())) {

            JobDataMap newJobDataMap = getJobDataMap(javaQuartz);
            JobDetail job =
                    JobBuilder.newJob(JavaTask.class).withIdentity(javaQuartz.getJobName(), javaQuartz.getJobGroup()).usingJobData(newJobDataMap).build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(javaQuartz.getJobName(), javaQuartz.getJobGroup())
                    .withSchedule(CronScheduleBuilder.cronSchedule(javaQuartz.getCronExpression()))
                    .build();
            try {
                //注册监听器
                //  scheduler.getListenerManager().addJobListener(new SimpleJobListener(), KeyMatcher.keyEquals(JobKey.jobKey("job1", "group1")));
                scheduler.getListenerManager().addJobListener(new JavaTaskListener(repository,scheduler), KeyMatcher.keyEquals(JobKey.jobKey(javaQuartz.getJobName(), javaQuartz.getJobGroup())));
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }

            javaQuartz.setJobStatus(JobStatusEnum.NEW.getCode());

            repository.flush();
        }

    }


    /**
     * 启动部分拓扑任务方法
     * @param ids
     */
    public void startTuoPuJobs(List<Long> ids)  {
        for (int i = 0;i < ids.size(); i++ ){

            startSingleTuoPuJob(ids.get(i));
            System.out.println("  startTuoPuJobs============"+ ids.get(i));

        }

    }

    /**
     * 启动所有拓扑任务方法
     * @param
     */
    public void startAllTuoPuJobs()  {
       List<JavaQuartz> javaQuartzList =repository.findByParentTaskIdAndJobStatus("-1",JobStatusEnum.ENABLED.getCode());
       List<Long> ids = new ArrayList<Long>();
       for (JavaQuartz javaQuartz:javaQuartzList){
           ids.add(javaQuartz.getId());
           System.out.println(" startAllTuoPuJobs============"+ javaQuartz.getId());
       }
       startTuoPuJobs(ids);

    }

}
