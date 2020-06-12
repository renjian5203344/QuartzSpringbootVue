package com.yizhan.listener;

import com.yizhan.dataobject.JavaQuartz;
import com.yizhan.enums.JobStatusEnum;
import com.yizhan.repository.JavaQuartzTaskRepository;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JavaTaskListener implements JobListener {

    JavaQuartzTaskRepository repository;

    /**
     * 构造函数，传入实体
     * @param repository
     */
    public JavaTaskListener(JavaQuartzTaskRepository repository){
        this.repository = repository;

    }


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
/****执行完毕获取一些信息
 * **/
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobDataMap map = context.getMergedJobDataMap();
        String name = map.getString("name");
        String group = map.getString("group");


        System.out.println("jobWasExecuted name :  " + name);
        System.out.println("jobWasExecuted group: {} " + group);


        JavaQuartz javaQuartz = repository.findByJobNameAndJobGroup(name,group);
        javaQuartz.setJobStatus(JobStatusEnum.FINISH.getCode());
        repository.save(javaQuartz);
        System.out.println("java打印"+ javaQuartz.toString());
        String jobName = context.getJobDetail().getKey().getName();
        System.out.println(jobName + " is going to be executed==by methdod ==jobWasExecuted");
    }
}
