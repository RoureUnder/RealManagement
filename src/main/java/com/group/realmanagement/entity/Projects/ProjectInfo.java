package com.group.realmanagement.entity.Projects;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;



import lombok.Data;

@Entity
@Data
public class ProjectInfo {
    @Id
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
