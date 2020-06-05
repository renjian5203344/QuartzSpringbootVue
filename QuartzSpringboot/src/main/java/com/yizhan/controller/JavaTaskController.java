package com.yizhan.controller;

import com.yizhan.dataobject.JavaQuartz;
import com.yizhan.service.JavaTaskservice;
import com.yizhan.trigger.Myjob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "javaTask")
@CrossOrigin
public class JavaTaskController {

    @Autowired
    JavaTaskservice javaTaskservice;

    @RequestMapping(value = "createJob",method = RequestMethod.POST)
    public void createJob(@RequestBody JavaQuartz javaQuartz){
        javaTaskservice.saveJavaTask(javaQuartz);
    }



    @RequestMapping(value = "pauseJob",method = RequestMethod.POST)
    public void pauseJob(@RequestBody JavaQuartz javaQuartz){
        javaTaskservice.pauseJavaTask(javaQuartz);
    }

}

