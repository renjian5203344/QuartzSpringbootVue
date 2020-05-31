package com.yizhan.repository;

import com.yizhan.dataobject.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersionRepository extends JpaRepository<Person, Long> {

}
