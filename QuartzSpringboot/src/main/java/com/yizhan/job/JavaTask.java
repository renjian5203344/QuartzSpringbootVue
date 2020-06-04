package com.yizhan.job;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@DisallowConcurrentExecution
@Component
public class JavaTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
        JobDataMap map = context.getMergedJobDataMap();
        String jarPath = map.getString("jarPath");//jar执行路径
        String parameter = map.getString("parameter");//参数

//        String param1 = parameter.split(" ")[0];
//        String param2 = parameter.split(" ")[1];


        String vmParam = map.getString("vmParam");//jvm参数

        String id = map.getString("id");
        System.out.println("Running Job name :  " + map.getString("name"));
        System.out.println("Running Job description : " + map.getString("JobDescription"));
        System.out.println("Running Job group: {} " + map.getString("group"));
        System.out.println("Running Job cron : " + map.getString("cronExpression"));
        System.out.println("Running Job jar path : {} " + jarPath);
        System.out.println("Running Job parameter : {} " + parameter);
        System.out.println("Running Job vmParam : {} " + vmParam);


        long startTime = System.currentTimeMillis();
        if (!StringUtils.isEmpty(jarPath)) {//如果jarPath不为空
            File jar = new File(jarPath); //实例化jar对象
            if (jar.exists()) {//如果路径存在
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(jar.getParentFile());

                List<String> commands = new ArrayList<String>();
                commands.add("java");

                if (!StringUtils.isEmpty(vmParam)){
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
                    logProcess(process.getInputStream(), process.getErrorStream());
                } catch (IOException e) {
                    throw new JobExecutionException(e);
                }
            } else throw new JobExecutionException("Job Jar not found >>  " + jarPath);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(">>>>>>>>>>>>> Running Job has been completed , cost time :  " + (endTime - startTime) + "ms\n");
    }


    //打印Job执行内容的日志
    private void logProcess(InputStream inputStream, InputStream errorStream) throws IOException {
        String inputLine;
        String errorLine;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
       while ((inputLine = inputReader.readLine()) != null) System.out.println(inputLine);
        while ((errorLine = errorReader.readLine()) != null) System.out.println(errorLine);
    }


}
