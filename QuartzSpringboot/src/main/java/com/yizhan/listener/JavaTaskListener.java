package com.yizhan.listener;

import com.yizhan.dataobject.JavaQuartz;
import com.yizhan.enums.JobStatusEnum;
import com.yizhan.job.JavaTask;
import com.yizhan.repository.JavaQuartzTaskRepository;
import com.yizhan.service.JavaTaskService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JavaTaskListener implements JobListener {

    private JavaQuartzTaskRepository repository;
    private Scheduler scheduler;

    /**
     * 构造函数，传入实体
     *
     * @param repository
     */
    public JavaTaskListener(JavaQuartzTaskRepository repository, Scheduler scheduler) {
        this.repository = repository;
        this.scheduler = scheduler;

    }


    @Override
    public String getName() {
        String name = getClass().getSimpleName();
        System.out.println(" listener name is:" + name);
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

    /****执行完毕获取一些信息
     * **/
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobDataMap map = context.getMergedJobDataMap();
        String name = map.getString("name");
        String group = map.getString("group");


        System.out.println("jobWasExecuted name :  " + name);
        System.out.println("jobWasExecuted group: {} " + group);


        JavaQuartz javaQuartz = repository.findByJobNameAndJobGroup(name, group);
        javaQuartz.setJobStatus(JobStatusEnum.FINISH.getCode());
        repository.save(javaQuartz);
        System.out.println("java打印" + javaQuartz.toString());
        String jobName = context.getJobDetail().getKey().getName();
        System.out.println(jobName + " is going to be executed==by methdod ==jobWasExecuted");

        //更新完父任务状态之后，启动子任务（子任务从数据库给查出来）
        String idsList = javaQuartz.getChildTaskId();
        String[] childStringList = idsList.split(",");
        List<Long> childIdsList = new ArrayList<Long>();
        for  (String id:childStringList){
            if (StringUtils.isNotBlank(id)){  //如果id不等于空字符串
                childIdsList.add(Long.valueOf(id));//Long.valueOf(id) String转化成long
            }
        }

        List<JavaQuartz> childList = repository.findByIdInAndJobStatus(childIdsList,JobStatusEnum.ENABLED.getCode());
//        List<JavaQuartz> childNewList =  new ArrayList<JavaQuartz>();

        if (childList != null && childList.size() > 0) {
//
//            for (JavaQuartz  entity: childList){
//
//                if (entity.getJobStatus()==JobStatusEnum.ENABLED.getCode()){
//                    childNewList.add(entity);
//                }
//            }
            startChildTask(childList);
        }

    }


    private void startChildTask(List<JavaQuartz> childList) {
        for (JavaQuartz javaQuartz : childList) {
            String ids = javaQuartz.getParentTaskId();
            String[] parentIds = ids.split(",");
            List<Long> parentidList = new ArrayList<Long>();
            for (String id: parentIds){
                if (StringUtils.isBlank(id)){
                    parentidList.add(Long.valueOf(id));
                }

            }

              List<JavaQuartz> parentList = repository.findByIdIn(parentidList);
              boolean parentHasCpmplete = true;  //标实


              for (JavaQuartz javaQuartz1:parentList){
                if (javaQuartz1.getJobStatus()!= JobStatusEnum.FINISH.getCode()){
                    System.out.println("还有父任务未完成！！！");
                     parentHasCpmplete = false;
                    break;
                }
              }
              if (!parentHasCpmplete){//如果false，继续循环
                  continue;
              }


            if (javaQuartz.getJobStatus() == JobStatusEnum.FINISH.getCode()) {
                System.out.println("该任务已经执行完毕！！！");
                return;

            }
            JobDataMap newJobDataMap = JavaTaskService.getJobDataMap(repository,javaQuartz);//构建数据对象newJobDataMap
            //构建JobDetail
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
                scheduler.getListenerManager().addJobListener(new JavaTaskListener(repository, this.scheduler), KeyMatcher.keyEquals(JobKey.jobKey(javaQuartz.getJobName(), javaQuartz.getJobGroup())));

                scheduler.scheduleJob(job, trigger);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}


