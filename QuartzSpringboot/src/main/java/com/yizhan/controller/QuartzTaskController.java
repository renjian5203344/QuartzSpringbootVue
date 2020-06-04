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


    @RequestMapping(value ="deletejob", method = RequestMethod.POST)
    public void deletejob(@RequestParam(value = "id") Long id){
        Quartz quartz = new Quartz();
        quartz.setId(id);
        repository.delete(quartz);

    }

    @RequestMapping(value ="updatejob", method = RequestMethod.POST)
    public void updatejob(@RequestBody Quartz quartz){
        repository.delete(quartz);
        repository.save(quartz);


    }
}
