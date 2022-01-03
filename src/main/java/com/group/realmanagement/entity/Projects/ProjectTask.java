package com.group.realmanagement.entity.Projects;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ProjectTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private int projectNo;
    private int taskStage;
    private String taskContent;
    private int senderNo;
    private int receiverNo;
    private Date sendTime;
    private Date processTime;
    private int status;
    private String reason;
    private int finished;
    private int projectFileNo;

    public ProjectTask(){}
    public ProjectTask(ProjectTaskReturn projectTaskReturn){
        this.no=projectTaskReturn.getNo();
        this.projectNo = projectTaskReturn.getProjectNo();
        this.taskStage= projectTaskReturn.getTaskStage();
        this.taskContent= projectTaskReturn.getTaskContent();
        this.senderNo= projectTaskReturn.getSenderNo();
        this.receiverNo= projectTaskReturn.getReceiverNo();
        this.sendTime= projectTaskReturn.getSendTime();
        this.processTime= projectTaskReturn.getProcessTime();
        this.status= projectTaskReturn.getStatus();
        this.reason= projectTaskReturn.getReason();
        this.finished= projectTaskReturn.getFinished();
        this.projectFileNo= projectTaskReturn.getProjectFileNo();
    }
}
