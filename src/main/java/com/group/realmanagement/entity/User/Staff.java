package com.group.realmanagement.entity.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private int staffNo;
    private String name;
    private String department;
    private String post;
    private String contact;
    private int access;
    private int status;
}
