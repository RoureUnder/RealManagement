package com.group.realmanagement.entity.Projects;

import java.sql.Date;

import com.group.realmanagement.repository.User.GuestRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;
@Data
public class ProjectInfoReturn {
    private int no;
    private int projectNo;
    private String projectName;
    private String guestName;
    private String principalName;
    private String modelName;
    private String renderName;
    private String lateName;
    private Date startTime;
    private Date endTime;
    private int status;

    public void setProjectDetail(ProjectInfo projectInfo,StaffRepository staffRepository,GuestRepository guestRepository) {
        this.no=projectInfo.getNo();
        this.projectNo=projectInfo.getProjectNo();
        this.projectName=projectInfo.getProjectName();
        this.guestName=guestRepository.findByGuestNo(projectInfo.getGuestNo()).getGuestName();
        this.principalName=staffRepository.findByStaffNo(projectInfo.getPrincipalNo()).getName();
        // staffRepository.findByStaffNo(projectInfo.getModelNo()).getName()
        this.modelName=staffRepository.findByStaffNo(projectInfo.getModelNo()).getName();
        this.renderName=staffRepository.findByStaffNo(projectInfo.getRenderNo()).getName();
        this.lateName=staffRepository.findByStaffNo(projectInfo.getLateNo()).getName();
        this.startTime=projectInfo.getStartTime();
        this.endTime=projectInfo.getEndTime();
        this.status=projectInfo.getStatus();
    }
}
