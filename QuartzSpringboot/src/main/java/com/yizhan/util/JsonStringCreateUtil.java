package com.yizhan.util;

import com.alibaba.fastjson.JSONObject;
import com.yizhan.dataobject.JavaQuartz;
import com.yizhan.dataobject.JavaTuoPuQuartz;

import java.util.ArrayList;
import java.util.List;

public class JsonStringCreateUtil {


    public static String getJsonString(JavaTuoPuQuartz javaTuoPuQuartz){
       String javaTuoPuQuartzString =  JSONObject.toJSONString(javaTuoPuQuartz);
       return  javaTuoPuQuartzString;

    }


    public static void main(String[] args) {
        JavaTuoPuQuartz javaTuoPuQuartzEntity = new JavaTuoPuQuartz();
        javaTuoPuQuartzEntity.setTuoPuName("复杂拓扑1");
        List<JavaQuartz> tuopuList = new ArrayList<JavaQuartz>();
        JavaQuartz javaQuartzEntityroot = new JavaQuartz();
        javaQuartzEntityroot.setJobName("rootjob1");
        javaQuartzEntityroot.setJobGroup("rootGroup1");
        javaQuartzEntityroot.setDescription("root1");
        javaQuartzEntityroot.setJarPath("/Users/newsan/yizhanJavaTask2.jar");
        javaQuartzEntityroot.setParameter("test1\t\ttest2");
        javaQuartzEntityroot.setVmParam("-Xms512m\t\t-Xmx512m\t\t-Xmn200m");
        javaQuartzEntityroot.setCronExpression("*/5 * * * * ?");
        javaQuartzEntityroot.setChildTaskId("");

        JavaQuartz javaQuartzEntitychild = new JavaQuartz();
        javaQuartzEntitychild.setJobName("childjob1");
        javaQuartzEntitychild.setJobGroup("childGroup1");
        javaQuartzEntitychild.setDescription("child1");
        javaQuartzEntitychild.setJarPath("/Users/newsan/yizhanJavaTask2.jar");
        javaQuartzEntitychild.setParameter("test1\t\ttest2");
        javaQuartzEntitychild.setVmParam("-Xms512m\t\t-Xmx512m\t\t-Xmn200m");
        javaQuartzEntitychild.setCronExpression("*/5 * * * * ?");
        javaQuartzEntitychild.setChildTaskId("");


        JavaQuartz javaQuartzEntityroot2 = new JavaQuartz();
        javaQuartzEntityroot2.setJobName("rootjob2");
        javaQuartzEntityroot2.setJobGroup("rootGroup2");
        javaQuartzEntityroot2.setDescription("root2");
        javaQuartzEntityroot2.setJarPath("/Users/newsan/yizhanJavaTask2.jar");
        javaQuartzEntityroot2.setParameter("test1\t\ttest2");
        javaQuartzEntityroot2.setVmParam("-Xms512m\t\t-Xmx512m\t\t-Xmn200m");
        javaQuartzEntityroot2.setCronExpression("*/5 * * * * ?");
        javaQuartzEntityroot2.setChildTaskId("");


        List<JavaQuartz> childlist = new ArrayList<JavaQuartz>();
        childlist.add(javaQuartzEntitychild);
        javaQuartzEntityroot.setChildList(childlist);

        javaQuartzEntitychild = new JavaQuartz();
        javaQuartzEntitychild.setJobName("childjob1");
        javaQuartzEntitychild.setJobGroup("childGroup1");
        javaQuartzEntitychild.setDescription("child1");
        javaQuartzEntitychild.setJarPath("/Users/newsan/yizhanJavaTask2.jar");
        javaQuartzEntitychild.setParameter("test1\t\ttest2");
        javaQuartzEntitychild.setVmParam("-Xms512m\t\t-Xmx512m\t\t-Xmn200m");
        javaQuartzEntitychild.setCronExpression("*/5 * * * * ?");
        javaQuartzEntitychild.setChildTaskId("");

        childlist = new ArrayList<JavaQuartz>();
        childlist.add(javaQuartzEntitychild);
        javaQuartzEntityroot2.setChildList(childlist);


        tuopuList.add(javaQuartzEntityroot);
        tuopuList.add(javaQuartzEntityroot2);

        javaTuoPuQuartzEntity.setJavaQuartzList(tuopuList);
        String result = getJsonString(javaTuoPuQuartzEntity);
        System.out.println(result);
    }
}
