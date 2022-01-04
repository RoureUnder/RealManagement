package com.group.realmanagement.entity.Projects;

import java.util.Date;

import com.group.realmanagement.entity.User.Guest;
import com.group.realmanagement.entity.User.Staff;
import com.group.realmanagement.repository.User.GuestRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import lombok.Data;
@Data
public class ProjectInfoReturn {
    private int no;
    private int projectNo;
    private String projectName;
    private int guestNo;
    private String guestName;
    private int principalNo;
    private String principalName;
    private int modelNo;
    private String modelName;
    private int modelStatus;
    private int renderNo;
    private String renderName;
    private int renderStatus;
    private int lateNo;
    private String lateName;
    private int lateStatus;
    private Date startTime;
    private Date endTime;
    private int status;
    private Double offer;


    public ProjectInfoReturn(){
        
    }
    public ProjectInfoReturn(ProjectInfo projectInfo,StaffRepository staffRepository,GuestRepository guestRepository){

        if(projectInfo.getGuestNo()==0){
            this.guestName = "未分配";
        }
        else{
            Guest guest = guestRepository.findByGuestNo(projectInfo.getGuestNo());
            if(guest!=null){
                this.guestName = guest.getGuestName();
            }
            else{
                this.guestName = "编号出错，联系管理员";
            }
        }

        if(projectInfo.getPrincipalNo()==0){
            this.principalName = "未分配";
        }
        else{
            Staff principal = staffRepository.findByStaffNo(projectInfo.getPrincipalNo());
            if(principal!=null){
                this.principalName = principal.getName();
            }
            else{
                this.principalName = "编号出错，联系管理员";
            }
        }

        if(projectInfo.getModelNo()==0){
            this.modelName = "未分配";
        }
        else{
            Staff model = staffRepository.findByStaffNo(projectInfo.getModelNo());
            if(model!=null){
                this.modelName = model.getName();
            }
            else{
                this.modelName = "编号出错，联系管理员";
            }
        }

        if(projectInfo.getRenderNo()==0){
            this.renderName = "未分配";
        }
        else{
            Staff render = staffRepository.findByStaffNo(projectInfo.getRenderNo());
            if(render!=null){
                this.renderName = render.getName();
            }
            else{
                this.renderName = "编号出错，联系管理员";
            }
        }

        if(projectInfo.getLateNo()==0){
            this.lateName = "未分配";
        }
        else{
            Staff late = staffRepository.findByStaffNo(projectInfo.getLateNo());
            if(late!=null){
                this.lateName = late.getName();
            }
            else{
                this.lateName = "编号出错，联系管理员";
            }
        }
        this.no = projectInfo.getNo();
        this.projectNo = projectInfo.getProjectNo();
        this.projectName = projectInfo.getProjectName();
        this.guestNo = projectInfo.getGuestNo();
        this.principalNo = projectInfo.getPrincipalNo();

        this.modelNo = projectInfo.getModelNo();

        this.modelStatus = projectInfo.getModelStatus();
        this.renderNo = projectInfo.getRenderNo();

        this.renderStatus = projectInfo.getRenderStatus();
        this.lateNo = projectInfo.getLateNo();

        this.lateStatus = projectInfo.getLateStatus();
        this.startTime = projectInfo.getStartTime();
        this.endTime = projectInfo.getEndTime();
        this.status = projectInfo.getStatus();
        this.offer = projectInfo.getOffer();
    }
}
