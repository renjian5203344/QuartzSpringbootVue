package com.yizhan.repository;

import com.yizhan.dataobject.QuartzEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuartzTaskRepository extends JpaRepository<QuartzEntity,Long>{

}
