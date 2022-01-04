package com.group.realmanagement.entity.Projects;

import java.util.Date;

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
    private int modelStatus;
    private int renderNo;
    private int renderStatus;
    private int lateNo;
    private int lateStatus;
    private Date startTime;
    private Date endTime;
    private int status;
    private Double offer;

    public ProjectInfo(){
        
    }
    public ProjectInfo(ProjectInfoReturn projectInfoReturn){
        this.no = projectInfoReturn.getNo();
        this.projectNo = projectInfoReturn.getProjectNo();
        this.projectName = projectInfoReturn.getProjectName();
        this.guestNo = projectInfoReturn.getGuestNo();
        this.principalNo = projectInfoReturn.getPrincipalNo();
        this.modelNo = projectInfoReturn.getModelNo();
        this.modelStatus = projectInfoReturn.getModelStatus();
        this.renderNo = projectInfoReturn.getRenderNo();
        this.renderStatus = projectInfoReturn.getRenderStatus();
        this.lateNo = projectInfoReturn.getLateNo();
        this.lateStatus = projectInfoReturn.getLateStatus();
        this.startTime = projectInfoReturn.getStartTime();
        this.endTime = projectInfoReturn.getEndTime();
        this.status = projectInfoReturn.getStatus();
        this.offer = projectInfoReturn.getOffer();
    }
}
