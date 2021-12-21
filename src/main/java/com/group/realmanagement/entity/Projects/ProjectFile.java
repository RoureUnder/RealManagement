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
    private String homeFloder;
    private String module;
    private String detail;
    private String fileName;
    private String fileSize;
    private String fullPath;
    private Date uploadTime;
    private int uploader;

    public void setDetailPath(ProjectInfoReturn projectInfoReturn,String fullPath,int uploader){
        this.projectNo=projectInfoReturn.getProjectNo();
        this.fullPath=fullPath;
        this.uploader = uploader;
        this.uploadTime = new Date();
        String eachPath[] = fullPath.split("/");
        this.homeFloder=eachPath[2];
        this.module=eachPath[3];
        if(JsonTest.getIndexOfPath(eachPath[4])==2){//有第三级目录
            this.detail=eachPath[4];
            this.fileName=eachPath[5];
        }
        else{//无第三级目录
            this.detail="";
            this.fileName=eachPath[4];
        }
    }
}
