package com.yizhan.repository;

import com.yizhan.dataobject.JavaQuartz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JavaQuartzTaskRepository extends JpaRepository<JavaQuartz,Long> {

    public JavaQuartz findByJobNameAndJobGroup(String jobName, String jobGroup);

    public List<JavaQuartz> findByParentTaskIdAndJobStatus(Long parentTaskId, Integer jobStatus);




}
