package com.yizhan.repository;

import com.yizhan.dataobject.JavaQuartz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JavaQuartzTaskRepository extends JpaRepository<JavaQuartz,Long> {

    public JavaQuartz findByJobNameAndJobGroup(String jobName, String jobGroup);

    public List<JavaQuartz> findByParentTaskIdAndJobStatus(String parentTaskId, Integer jobStatus);


   public List<JavaQuartz> findByParentTaskId (String ParentTaskId);

    public List<JavaQuartz> findByIdIn(List<Long> idList);


    public List<JavaQuartz> findByIdInAndJobStatus(List<Long> idList,Integer jobStatus);





}
