package com.yizhan.repository;

import com.yizhan.dataobject.Quartz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuartzTaskRepository extends JpaRepository<Quartz,Long>{

}
