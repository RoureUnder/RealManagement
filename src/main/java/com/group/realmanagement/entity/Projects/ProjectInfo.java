package com.group.realmanagement.entity.Projects;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;



import lombok.Data;

@Entity
@Data
public class ProjectInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private int projectNo;
    private String projectName;
    private int guestNo;
    private int principalNo;
    private int modelNo;
    private int renderNo;
    private int lateNo;
    private Date startTime;
    private Date endTime;
    private int status;
}
