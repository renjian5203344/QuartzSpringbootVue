package com.yizhan.controller;

import com.yizhan.dataobject.Person;
import com.yizhan.repository.PersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "testJpa")
public class TestPersonController {

    @Autowired
    private PersionRepository repository;

    @PostMapping(path = "addPerson")
    public void addPerson(Person person) {
        System.out.println(person.toString());
        repository.save(person);
    }

    @DeleteMapping(path = "deletePerson")
    public void deletePerson(Long id) {
        Person person = new Person();
        person.setId(id);
        repository.delete(person);
    }
}
