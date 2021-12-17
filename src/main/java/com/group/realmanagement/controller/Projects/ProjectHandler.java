package com.group.realmanagement.controller.Projects;

import java.util.ArrayList;
import java.util.List;

import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.entity.Projects.ProjectInfoReturn;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/project")
public class ProjectHandler {
    @Autowired
    private ProjectInfoRepository projectInfoRepository;
    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/findAll")
    List<ProjectInfoReturn> findAll(){
        List<ProjectInfo> projectInfos = projectInfoRepository.findAll();
        List<ProjectInfoReturn> projectInfoReturns = new ArrayList<>();
        for (ProjectInfo projectInfo : projectInfos) {
            ProjectInfoReturn tempProjectInfoReturn = new ProjectInfoReturn();
            tempProjectInfoReturn.setNo(projectInfo.getNo());
            tempProjectInfoReturn.setProjectNo(projectInfo.getProjectNo());
            tempProjectInfoReturn.setProjectName(projectInfo.getProjectName());
            tempProjectInfoReturn.setGuestName("testGuestName");
            tempProjectInfoReturn.setPrincipalName(staffRepository.findByStaffNo(projectInfo.getPrincipalNo()).getName());
            tempProjectInfoReturn.setModelName(staffRepository.findByStaffNo(projectInfo.getModelNo()).getName());
            tempProjectInfoReturn.setRenderName(staffRepository.findByStaffNo(projectInfo.getRenderNo()).getName());
            tempProjectInfoReturn.setLateName(staffRepository.findByStaffNo(projectInfo.getLateNo()).getName());
            tempProjectInfoReturn.setStartTime(projectInfo.getStartTime());
            tempProjectInfoReturn.setEndTime(projectInfo.getEndTime());
            tempProjectInfoReturn.setStatus(projectInfo.getStatus());
            projectInfoReturns.add(tempProjectInfoReturn);
        }
        return projectInfoReturns;
    }
}
