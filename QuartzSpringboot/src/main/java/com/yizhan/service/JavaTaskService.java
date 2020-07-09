package com.yizhan.service;
import com.yizhan.dataobject.JavaTuoPuQuartz;
import com.yizhan.repository.JavaTuoPuQuartzRepository;
import org.quartz.*;
import com.yizhan.dataobject.JavaQuartz;
import com.yizhan.enums.JobStatusEnum;
import com.yizhan.job.JavaTask;
import com.yizhan.listener.JavaTaskListener;
import com.yizhan.repository.JavaQuartzTaskRepository;
import com.yizhan.util.FileReadAndWriteUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class JavaTaskService {

    @Autowired
    JavaQuartzTaskRepository javaQuartzTaskRepository;

    @Autowired
    JavaTuoPuQuartzRepository javaTuoPuQuartzRepository;

    @Autowired
    private Scheduler scheduler;

    public void saveJavaTask(JavaQuartz javaQuartz) {//入参JavaQuartz实体
        if (StringUtils.isBlank(javaQuartz.getParentTaskId())) {
            javaQuartz.setParentTaskId("-1");

        }

        JavaQuartz result = javaQuartzTaskRepository.findByJobNameAndJobGroup(javaQuartz.getJobName(), javaQuartz.getJobGroup());
        if (result != null) {
            System.out.println("jobName 和　jobGroup已经存在");
            return;

        }
        javaQuartz.setJobStatus(JobStatusEnum.NEW.getCode());
        javaQuartzTaskRepository.save(javaQuartz);
        //service在构建map时，需要处理下
        JobDataMap newJobDataMap = getJobDataMap(javaQuartzTaskRepository, javaQuartz);//构建数据对象newJobDataMap
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
            scheduler.getListenerManager().addJobListener(new JavaTaskListener(javaQuartzTaskRepository, scheduler), KeyMatcher.keyEquals(JobKey.jobKey(javaQuartz.getJobName(), javaQuartz.getJobGroup())));

            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }


    public void pauseJavaTask(JavaQuartz javaQuartz) {

//        Optional<JavaQuartz> optional = repository.findById(javaQuartz.getId());
//        JavaQuartz javaQuartz1 = optional.get();
        JavaQuartz javaQuartz1 = javaQuartzTaskRepository.getOne(javaQuartz.getId());

        if (javaQuartz1 != null) {

//            repository.deleteById(javaQuartz.getId());
            javaQuartz1.setJobStatus(JobStatusEnum.PAUSE.getCode());
//            repository.save(javaQuartz1);

            javaQuartzTaskRepository.flush();
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
        JavaQuartz javaQuartz1 = javaQuartzTaskRepository.getOne(javaQuartz.getId());

        if (javaQuartz1 != null) {

            javaQuartz1.setJobStatus(JobStatusEnum.RESUME.getCode());

            javaQuartzTaskRepository.flush();
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


    public static JobDataMap getJobDataMap(JavaQuartzTaskRepository repository, JavaQuartz javaQuartz) {
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
        JavaQuartz result = repository.findByJobNameAndJobGroup(javaQuartz.getJobName(), javaQuartz.getJobGroup());
        jobDataMap.put("id", result.getId() + "");//转成字符串

        return jobDataMap;
//
//


    }

    /****
     * 创建拓扑任务
     * @param javaQuartz  实体
     */


    public void createTuoPuJavaTask(JavaQuartz javaQuartz) {

        javaQuartz.setJobStatus(JobStatusEnum.ENABLED.getCode());

        if (StringUtils.isBlank(javaQuartz.getParentTaskId())) { //如果parentTaskId为空
            javaQuartz.setParentTaskId("-1");

        }


        JavaQuartz javaQuartz11 = javaQuartzTaskRepository.findByJobNameAndJobGroup(javaQuartz.getJobName(), javaQuartz.getJobGroup());
        if (javaQuartz11 != null) {
            System.out.println("jobName 和　jobGroup已经存在");
            return;

        }


        javaQuartzTaskRepository.save(javaQuartz);
        JavaQuartz javaquartz33 = javaQuartzTaskRepository.findByJobNameAndJobGroup(javaQuartz.getJobName(), javaQuartz.getJobGroup());
        String quartzEntityId = javaquartz33.getId() + "";
        String parentTaskId = javaQuartz.getParentTaskId();
        if (!parentTaskId.equals("-1")) {
            String[] parentids = parentTaskId.split(",");
            for (String id : parentids) {
                if (StringUtils.isNotBlank(id)) {
                    JavaQuartz javaQuartz22 = javaQuartzTaskRepository.findById(Long.valueOf(id)).get();
                    String preChild = javaQuartz22.getChildTaskId();
                    String[] prechildString = preChild.split(",");
                    boolean isAdd = true;
                    for (String prechildid : prechildString) {
                        if (prechildid.equals(quartzEntityId)) {
                            isAdd = false;
                            break;
                        }
                    }
                    if (isAdd) {
                        preChild += preChild + "," + quartzEntityId;
                    }
                    javaQuartz22.setChildTaskId(preChild);
                    javaQuartzTaskRepository.save(javaQuartz22);
                }
            }
        }
    }

    /****
     * 更新拓扑任务
     * @param javaQuartz  实体
     */
    public void updateTuoPuJavaTask(JavaQuartz javaQuartz) {

        javaQuartz.setJobStatus(JobStatusEnum.ENABLED.getCode());

        javaQuartzTaskRepository.save(javaQuartz);
        JavaQuartz javaquartz33 = javaQuartzTaskRepository.findByJobNameAndJobGroup(javaQuartz.getJobName(), javaQuartz.getJobGroup());
        String quartzEntityId = javaquartz33.getId() + "";
        String parentTaskId = javaQuartz.getParentTaskId();
        if (!parentTaskId.equals("-1")) {
            String[] parentids = parentTaskId.split(",");
            for (String id : parentids) {
                if (StringUtils.isNotBlank(id)) {
                    //找到父节点
                    JavaQuartz javaQuartz22 = javaQuartzTaskRepository.findById(Long.valueOf(id)).get();
                    //得到之前的
                    String preChild = javaQuartz22.getChildTaskId();
                    String[] prechildString = preChild.split(",");
                    boolean isAdd = true;
                    for (String prechildid : prechildString) {
                        if (prechildid.equals(quartzEntityId)) {  //如果已经加了就不加了，没加的话添加上
                            isAdd = false;
                            break;
                        }
                    }
                    if (isAdd) {
                        preChild += preChild + "," + quartzEntityId;
                    }
                    javaQuartz22.setChildTaskId(preChild);
                    javaQuartzTaskRepository.save(javaQuartz22);
                }
            }
        }
    }


    /**
     * 启动单个拓扑任务方法
     *
     * @param id
     */
    public void startSingleTuoPuJob(Long id) {

        JavaQuartz javaQuartz = javaQuartzTaskRepository.getOne(id);

        if (javaQuartz != null && javaQuartz.getJobStatus() == JobStatusEnum.ENABLED.getCode() && "-1".equals(javaQuartz.getParentTaskId())) {

            JobDataMap newJobDataMap = getJobDataMap(javaQuartzTaskRepository, javaQuartz);
            JobDetail job =
                    JobBuilder.newJob(JavaTask.class).withIdentity(javaQuartz.getJobName(), javaQuartz.getJobGroup()).usingJobData(newJobDataMap).build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(javaQuartz.getJobName(), javaQuartz.getJobGroup())
                    .withSchedule(CronScheduleBuilder.cronSchedule(javaQuartz.getCronExpression()))
                    .build();
            try {
                //注册监听器
                //  scheduler.getListenerManager().addJobListener(new SimpleJobListener(), KeyMatcher.keyEquals(JobKey.jobKey("job1", "group1")));
                scheduler.getListenerManager().addJobListener(new JavaTaskListener(javaQuartzTaskRepository, scheduler), KeyMatcher.keyEquals(JobKey.jobKey(javaQuartz.getJobName(), javaQuartz.getJobGroup())));
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }

            javaQuartz.setJobStatus(JobStatusEnum.NEW.getCode());

            javaQuartzTaskRepository.flush();
        }

    }


    /**
     * 启动部分拓扑任务方法
     *
     * @param ids
     */
    public void startTuoPuJobs(List<Long> ids) {
        for (int i = 0; i < ids.size(); i++) {

            startSingleTuoPuJob(ids.get(i));
            System.out.println("  startTuoPuJobs============" + ids.get(i));

        }

    }

    /**
     * 启动所有拓扑任务方法
     *
     * @param
     */
    public void startAllTuoPuJobs() {
        List<JavaQuartz> javaQuartzList =
                javaQuartzTaskRepository.findByParentTaskIdAndJobStatus("-1", JobStatusEnum.ENABLED.getCode());
        List<Long> ids = new ArrayList<Long>();
        for (JavaQuartz javaQuartz : javaQuartzList) {
            ids.add(javaQuartz.getId());
            System.out.println(" startAllTuoPuJobs============" + javaQuartz.getId());
        }
        startTuoPuJobs(ids);

    }

    /**
     * 查询单个任务列表
     *
     * @param
     */
    public List<JavaQuartz> listAllAloneTask() {
        return javaQuartzTaskRepository.findByParentTaskId("-1");

    }


    /**
     * 读取任务信息方法
     *
     * @param
     */
    public String viewInfobyId(Long id) {
        String result = "没有输出";
        try {
            result = FileReadAndWriteUtil.ReadFromFile(id + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /****
     * 构建Map<JavaQuartz, Set<JavaQuartz>>
     * @param parentMap
     * @param parent
     * @param self
     */

    public void createTuoPuBy(Map<JavaQuartz, Set<JavaQuartz>> parentMap, JavaQuartz parent, JavaQuartz self) {
        if (parent != null) {//如果父对象不等于空
            Set<JavaQuartz> setpre = parentMap.get(self);//通过self对象取得之前的setpre对象
            if (setpre == null) {//如果之前等于空
                setpre = new HashSet<JavaQuartz>();//初始化
            }

            setpre.add(parent);//把parent对象添加到Set<JavaQuartz>集合之中

            parentMap.put(self, setpre);

        } else {//如果没有父节点

            Set<JavaQuartz> set = new HashSet<JavaQuartz>();
            parentMap.put(self, set);//self为本节点
        }
        //获取子节点
        List<JavaQuartz> childList = self.getChildList();
        if (childList == null) {//如果等于null，直接return
            return;

        } else {//如果不等于null，循环遍历
            for (JavaQuartz javaQuartz : childList) {
                createTuoPuBy(parentMap, self, javaQuartz);//传入parentMap，self作为parent,javaQuartz未本节点
            }
        }

    }

    /**
     * 构建完Map之后，重新渲染拓扑结构数据
     *
     * @param parentMap
     */

    public List<JavaQuartz> render(Map<JavaQuartz, Set<JavaQuartz>> parentMap) {
        List<JavaQuartz> rootJavaQuartz = new ArrayList<JavaQuartz>();
        Set<Map.Entry<JavaQuartz, Set<JavaQuartz>>> sets = parentMap.entrySet();
        for (Map.Entry<JavaQuartz, Set<JavaQuartz>> entry : sets){

            JavaQuartz self = entry.getKey();  //取得本结点
            Set<JavaQuartz> parents = entry.getValue();//取得父结点

            if (parents.isEmpty()) {//如果parents为空,说明是root
                createTuoPuJavaTask(self);//调用createTuoPuJavaTask()
                JavaQuartz selfNew = javaQuartzTaskRepository.findByJobNameAndJobGroup(self.getJobName(), self.getJobGroup());
                rootJavaQuartz.add(selfNew);

            }else {//如果parents不为空（不是根节点）

                String parentids = "";

                for (JavaQuartz javaQuartz : parents){
                    JavaQuartz parent = javaQuartzTaskRepository.findByJobNameAndJobGroup(javaQuartz.getJobName(), javaQuartz.getJobGroup());
                       if (parent == null){
                           createTuoPuJavaTask(javaQuartz);
                           parent = javaQuartzTaskRepository.findByJobNameAndJobGroup(javaQuartz.getJobName(), javaQuartz.getJobGroup());

                       }

                    //拼接parentids
                    parentids += parent.getId() + ",";

                }
                JavaQuartz child = javaQuartzTaskRepository.findByJobNameAndJobGroup(self.getJobName(), self.getJobGroup());
                if (child != null){
                    child.setParentTaskId(child.getParentTaskId() + parentids);
                    updateTuoPuJavaTask(child);

                }else {
                    self.setParentTaskId(parentids);//渲染父节点的id
                    createTuoPuJavaTask(self);

                }

            }

        }
        return rootJavaQuartz;
    }

    /****
     *  查看TuoPu总的信息
     * @param javaTuoPuQuartz  实体对象
     */

    public void createTuoPuTotalInfo(JavaTuoPuQuartz javaTuoPuQuartz) {
        javaTuoPuQuartzRepository.save(javaTuoPuQuartz);



    }






}
