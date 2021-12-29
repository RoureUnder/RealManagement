package com.group.realmanagement.entity.Projects;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ProjectFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private int projectNo;
    private int taskNo;
    private String homeFloder;
    private String module;
    private String detail;
    private String fileName;
    private String fileSize;
    private String fullPath;
    private Date uploadTime;
    private int uploader;

    public ProjectFile(ProjectFileReview projectFileReview) {
        this.projectNo = projectFileReview.getProjectNo();
        this.taskNo = projectFileReview.getTaskNo();
        this.homeFloder = projectFileReview.getHomeFloder();
        this.module = projectFileReview.getModule();
        this.detail = projectFileReview.getDetail();
        this.fileName = projectFileReview.getFileName();
        this.fileSize = projectFileReview.getFileSize();
        this.fullPath = projectFileReview.getFullPath();
        this.uploader = projectFileReview.getUploader();
        this.uploadTime = projectFileReview.getUploadTime();
    }

    public ProjectFile() {
    }

    public void setDetailPath(ProjectInfo projectInfo, String fullPath, int uploader ,int taskNo) {
        this.projectNo = projectInfo.getProjectNo();
        this.taskNo = taskNo;
        this.fullPath = fullPath;
        this.uploader = uploader;
        this.uploadTime = new Date();
        String eachPath[] = fullPath.split("/");
        this.homeFloder = eachPath[2];
        this.module = eachPath[3];
        if (JsonTest.getIndexOfPath(eachPath[4]) == 2) {// 有第三级目录
            this.detail = eachPath[4];
            this.fileName = eachPath[5];
        } else {// 无第三级目录
            this.detail = "";
            this.fileName = eachPath[4];
        }
    }
}
