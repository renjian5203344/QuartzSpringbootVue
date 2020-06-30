package com.yizhan.util;
import java.io.*;

/***
 * 监控信息保存到文件、读取文件工具类
 */
public class FileReadAndWriteUtil {
    static String filePath ="/Users/newsan/workspace/test/quartzfile";
    /***
     *监控信息保存到文件
     * @param content  日志任务输出信息
     * @param id   任务id
     */
    public static void wirteToFile(String content,String id){
        //拼接文件全路径，fileNamePath
        String fileNamePath = filePath+ "/"+id+"info.txt";//每个任务放到以id为前缀，.txt为后缀的文件中
        if (!new File(fileNamePath).exists()){
            try {
                new File(fileNamePath).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileNamePath,true);
            fileWriter.write(content+"\r\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {//需要关掉
            if (fileWriter!=null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    /**
     * 读取文件
     * @param id
     */

    public static String ReadFromFile(String id){
        //拼接文件全路径，fileNamePath
        String fileNamePath = filePath+ "/"+id+"info.txt";//每个任务放到以id为前缀，.txt为后缀的文件中

        BufferedReader bufferedReader = null;
        String result = "";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileNamePath)));
            String tmp ="";

           while ((tmp = bufferedReader.readLine())!=null){
               result += tmp;

           }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {//需要关掉
            if ( bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
           return result;
    }


    public static void main(String args[]){
//        wirteToFile("test","45");
        String result = ReadFromFile("45");
        System.out.println(result);
    }


}
