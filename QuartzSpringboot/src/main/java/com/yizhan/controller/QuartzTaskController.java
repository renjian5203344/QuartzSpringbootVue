package com.yizhan.controller;

import com.yizhan.dataobject.QuartzEntity;
import com.yizhan.repository.QuartzTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "quartzTask")
public class QuartzTaskController {

    @Autowired
    private QuartzTaskRepository repository;


    @RequestMapping(value ="createjob", method = RequestMethod.POST)
    public void createjob(@RequestBody QuartzEntity quartz){
        repository.save(quartz);

    }


    @RequestMapping(value ="deletejob", method = RequestMethod.POST)
    public void deletejob(@RequestParam(value = "id") Long id){
        QuartzEntity quartz = new QuartzEntity();
        quartz.setId(id);
        repository.delete(quartz);

    }

    @RequestMapping(value ="updatejob", method = RequestMethod.POST)
    public void updatejob(@RequestBody QuartzEntity quartz){
        repository.delete(quartz);
        repository.save(quartz);


    }
}
