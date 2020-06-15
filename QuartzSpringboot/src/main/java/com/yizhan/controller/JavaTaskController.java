package com.yizhan.controller;
import com.yizhan.enums.ResultEnum;
import com.yizhan.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import com.yizhan.VO.ResultVO;
import com.yizhan.dataobject.JavaQuartz;
import com.yizhan.service.JavaTaskService;
import com.yizhan.util.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/javaTask")
@CrossOrigin
@Slf4j
public class JavaTaskController {

    @Autowired
    JavaTaskService javaTaskservice;

    @PostMapping( value = "/createJob")
    public ResultVO createJob(@Valid @RequestBody JavaQuartz javaQuartz, BindingResult bindingResult){

     if (bindingResult.hasErrors()){
         log.error("【创建job】参数不正确, javaQuartz={}",javaQuartz);
         throw  new SystemException(ResultEnum.PARAM_ERROR.getCode(),
                 bindingResult.getFieldError().getDefaultMessage());
     }
        javaTaskservice.saveJavaTask(javaQuartz);

        return ResultVOUtil.success();
    }


    /****
     * 创建单个任务，没有拓扑结构
     * @param javaQuartz
     * @return
     */
    @PostMapping( value = "/pauseJob")
    public ResultVO pauseJob(@RequestBody JavaQuartz javaQuartz){
        javaTaskservice.pauseJavaTask(javaQuartz);
        return ResultVOUtil.success();
    }

    @PostMapping( value = "/resumeJavaTask")
    public  ResultVO resumeJob(@RequestBody JavaQuartz javaQuartz){
        javaTaskservice.resumeJavaTask(javaQuartz);
        return ResultVOUtil.success();
    }


    @PostMapping( value = "/createTuoPuJob")
    public  ResultVO createTuoPuJob(@RequestBody JavaQuartz javaQuartz,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【创建拓扑job】参数不正确, javaQuartz={}",javaQuartz);
            throw  new SystemException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        javaTaskservice.createTuoPuJavaTask(javaQuartz);

        return ResultVOUtil.success();
    }


    @PostMapping( value = "/startSingleTuoPuJob")
    public  ResultVO startSingleTuoPuJob(@RequestBody JavaQuartz javaQuartz){
        Long id = javaQuartz.getId();
        javaTaskservice.startSingleTuoPuJob(id);
        return ResultVOUtil.success();
    }




    //startAllTuoPuJob可以启动所有，可以指定部分
    @PostMapping( value = "/startAllTuoPuJob")
    public  ResultVO startAllTuoPuJob(){
        javaTaskservice.startAllTuoPuJobs();
        return ResultVOUtil.success();

    }


}

