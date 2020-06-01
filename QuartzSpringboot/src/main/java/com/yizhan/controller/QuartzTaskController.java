package com.yizhan.controller;

import com.yizhan.dataobject.Quartz;
import com.yizhan.repository.QuartzTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "quartzTask")
public class QuartzTaskController {

    @Autowired
    private QuartzTaskRepository repository;


    @RequestMapping(value ="createjob", method = RequestMethod.POST)
    public void createjob(@RequestBody Quartz quartz){
        repository.save(quartz);

    }
}
