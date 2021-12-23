package com.group.realmanagement.entity.Projects;


import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
@Data
@Entity
public class ProjectFileReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private int projectNo;
    private String homeFloder;
    private String module;
    private String detail;
    private String fileName;
    private String fileSize;
    private String fullPath;
    private int uploader;
    private Date uploadTime;
    private int principalNo;
    private int status;
    private String suggestion;

    public void setReviewByFile(ProjectFile projectFile,int principalNo){
        this.projectNo=projectFile.getProjectNo();
        this.homeFloder=projectFile.getHomeFloder();
        this.module = projectFile.getModule();
        this.detail = projectFile.getDetail();
        this.fileName = projectFile.getFileName();
        this.fileSize = projectFile.getFileSize();
        this.fullPath = projectFile.getFullPath();
        this.uploader = projectFile.getUploader();
        this.uploadTime = projectFile.getUploadTime();
        this.principalNo = principalNo;
        this.status=0;
        this.suggestion="";
    }
}
