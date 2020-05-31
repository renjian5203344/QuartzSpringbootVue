package com.yizhan.dataobject;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = true,length = 20)
    private String name;

    @Column(name = "age",nullable = true,length = 20)
    private int age;

}
