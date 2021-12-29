package com.group.realmanagement.entity.Projects;

import java.util.Date;

import com.group.realmanagement.entity.User.Staff;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import lombok.Data;

@Data
public class ProjectTaskReturn {
    private int no;
    private int projectNo;
    private String projectName;
    private int taskStage;
    private String taskContent;
    private int senderNo;
    private String senderName;
    private int receiverNo;
    private String receiverName;
    private Date sendTime;
    private Date processTime;
    private int status;
    private String reason;
    private int finished;
    private int projectFileNo;

    public ProjectTaskReturn(){

    }
    public ProjectTaskReturn(ProjectTask projectTask, ProjectInfoRepository projectInfoRepository, StaffRepository staffRepository){
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectTask.getProjectNo());
        Staff sender = staffRepository.findByStaffNo(projectTask.getSenderNo());
        Staff receiver = staffRepository.findByStaffNo(projectTask.getReceiverNo());
        this.no=projectTask.getNo();
        this.projectNo = projectTask.getProjectNo();
        if(projectInfo!=null){
            this.projectName = projectInfo.getProjectName();
        }
        else{
            this.projectName = "名称获取出错";
        }
        this.taskStage= projectTask.getTaskStage();
        this.taskContent= projectTask.getTaskContent();
        this.senderNo= projectTask.getSenderNo();
        if(sender!=null){
            this.senderName= sender.getName();
        }
        else{
            this.senderName= "发送者信息获取错误";
        }
        this.receiverNo= projectTask.getReceiverNo();
        if(receiver!=null){
            this.receiverName= receiver.getName();
        }
        else{
            this.receiverName= "发送者信息获取错误";
        }
        this.sendTime= projectTask.getSendTime();
        this.processTime= projectTask.getProcessTime();
        this.status= projectTask.getStatus();
        this.reason= projectTask.getReason();
        this.finished= projectTask.getFinished();
        this.projectFileNo= projectTask.getProjectFileNo();
    }
}
