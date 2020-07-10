package com.yizhan.job;
import com.yizhan.dataobject.JavaQuartz;
import com.yizhan.enums.JobStatusEnum;
import com.yizhan.repository.JavaQuartzTaskRepository;
import com.yizhan.util.DateUtil;
import com.yizhan.util.FileReadAndWriteUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.threads.TaskThread;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

@DisallowConcurrentExecution
@Component
public class JavaTask implements Job {

    public JavaTask(){
        new Thread(new MyTaskView(queue)).start();
    }


    LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<String>();

    @Autowired
    JavaQuartzTaskRepository repository;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
        JobDataMap map = context.getMergedJobDataMap();
        String jarPath = map.getString("jarPath");
        String parameter = map.getString("parameter");
        String vmParam = map.getString("vmParam");
        String id = map.getString("id");

        System.out.println("Running Job name :  " + map.getString("name"));
        System.out.println("Running Job description : " + map.getString("JobDescription"));
        System.out.println("Running Job group: {} " + map.getString("group"));
        System.out.println("Running Job cron : " + map.getString("cronExpression"));
        System.out.println("Running Job jar path : {} " + jarPath);
        System.out.println("Running Job parameter : {} " + parameter);
        System.out.println("Running Job vmParam : {} " + vmParam);

        if (!canStart(map.getString("name"), map.getString("group"))){//如果不能跑
            return;

        }


        long startTime = System.currentTimeMillis();

        if (!StringUtils.isEmpty(jarPath)) {//如果jarPath不为空

            File jar = new File(jarPath); //实例化jar对象

            if (jar.exists()) {//如果路径存在

                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(jar.getParentFile());

                List<String> commands = new ArrayList<String>();
                commands.add("java");

                if (!StringUtils.isEmpty(vmParam)){//如果vmParam不为空
                  String[] vmParamArray =vmParam.split("\t\t");
                  for (String vmPara: vmParamArray){

                      commands.add(vmPara);
                  }
                }

                commands.add("-jar");
                commands.add(jarPath);


                if (!StringUtils.isEmpty(parameter)){
                    String[] paramArray = parameter.split("\t\t");
                    for (String param: paramArray){
                        commands.add(param);
                    }

                }
                processBuilder.command(commands);

                System.out.println("Running Job details as follows >>>>>>>>>>>>>>>>>>>>: ");
                System.out.println("Running Job commands : {}  " + commands.toString());
                try {
                    Process process = processBuilder.start();// process进程对象
                    //这里会执行命令，命令会输出，输出的时候，根据id生成文件（输出内容保存到文件里面去）
                    logProcess(process.getInputStream(), process.getErrorStream(),id);
                } catch (IOException e) {
                    throw new JobExecutionException(e);
                }
            } else throw new JobExecutionException("Job Jar not found >>  " + jarPath);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>>>> Running Job has been completed , cost time :  " + (endTime - startTime) + "ms\n");
    }


    //打印Job执行内容的日志
    private void logProcess(InputStream inputStream, InputStream errorStream,String id) throws IOException {
        String inputLine;
        String errorLine;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
       while ((inputLine = inputReader.readLine()) != null) {
           try {
               queue.put(id+"======"+inputLine+"======"+DateUtil.getCurrentString());
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           System.out.println(inputLine);
       }
        while ((errorLine = errorReader.readLine()) != null) {
            try {
                queue.put(id+"======"+errorLine+"======"+DateUtil.getCurrentString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(errorLine);
        }
    }


    /***
     *   判断是否执行
     * @param jobName   任务名称
     * @param groupName  任务分组
     * @return  布尔类型  返回true表示可以执行，返回false表示不可以执行
     */
    private boolean canStart(String jobName,String groupName){
        JavaQuartz javaQuartz = repository.findByJobNameAndJobGroup(jobName,groupName);//首先根据jobName和groupName查询出javaQuartz对象

        if (javaQuartz == null){
            return  false;
       }

        String parentTaskId = javaQuartz.getParentTaskId();//取javaQuartz对象的ParentTaskId
       if ("-1".equals(parentTaskId)){//如果parentTaskId等于-1
           if (javaQuartz.getJobStatus()!=JobStatusEnum.FINISH.getCode()){
               return  true; //返回true,让它跑
           }
       }else {//如果parentTaskId不等于-1
           String[] parentIds = parentTaskId.split(",");
           List<Long> parentIdList = new ArrayList<Long>();
           for (String id: parentIds){//循环parentIds
               if (StringUtils.isBlank(id)){ //如果id不等于空字符串
                   parentIdList.add(Long.valueOf(id));
               }
           }

           List<JavaQuartz> parentList = repository.findByIdIn(parentIdList);

           boolean allComplete = true;  //标记，是否都完成，默认为true
           for (JavaQuartz entity: parentList){

               if (entity.getJobStatus() != JobStatusEnum.FINISH.getCode()){ //如果如果父任务的jobStatus等于4
                   allComplete = false;
               }

           }

              if (allComplete){ //如果所有都执行完，返回true,否则返回false
                  return true;
              }else {
                  return false;
              }
       }
       return false;
    }


    /***
     * 内部类
     */
    class  MyTaskView implements Runnable {
        private LinkedBlockingDeque<String> queue;
        public MyTaskView(LinkedBlockingDeque<String> queue){
            this.queue = queue;
        }


      @Override
      public void run() {
                try {
                    while (true) {
                    String lines = queue.take();
                    //多线程取的时候，进行分割
                   String[] linesArray = lines.split("======");
                   String id = linesArray[0];
                   String contents = linesArray[1];
                   String timeString = linesArray[2];
                   contents = timeString+"\t"+contents;
                   //取到id和contents之后保存文件
                   FileReadAndWriteUtil.writeToFile(contents,id);

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

      }


}
