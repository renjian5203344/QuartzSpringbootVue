package com.yizhan.trigger;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**自定义作业类
 */
public class Myjob  implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {

//        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
//        String name = (String) jobDataMap.get("name");
//        System.out.println("收到的参数是：" + name);
        System.out.println("当前作业开始执行了，作业的时间是："+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));


    }
}
