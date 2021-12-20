package com.group.realmanagement.controller.Projects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.JsonTest;
import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.entity.Projects.ProjectInfoReturn;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.User.GuestRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/project/info")
public class ProjectInfoHandler {
    @Autowired
    private ProjectInfoRepository projectInfoRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private GuestRepository guestRepository;

    @GetMapping("/findAll")
    JSONObject findAll(){
        JSONObject jObject = new JSONObject();
        List<ProjectInfo> projectInfos = projectInfoRepository.findAll();
        List<ProjectInfoReturn> projectInfoReturns = new ArrayList<>();
        for (ProjectInfo projectInfo : projectInfos) {
            ProjectInfoReturn tempProjectInfoReturn = new ProjectInfoReturn();
            tempProjectInfoReturn.setProjectDetail(projectInfo, staffRepository, guestRepository);
            projectInfoReturns.add(tempProjectInfoReturn);
        }
        if(projectInfoReturns.size()!=0){
            jObject.put("ProjectInfos", projectInfoReturns);
            jObject.put("Message", "success");
        }
        else{
            jObject.put("Message", "error");
        }
        return jObject;
    }

    @GetMapping("/findByProjectNo")
    JSONObject findByProjectNo(int projectNo){
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);
        ProjectInfoReturn projectInfoReturn = new ProjectInfoReturn();
        projectInfoReturn.setProjectDetail(projectInfo, staffRepository, guestRepository);
        if(projectInfo!=null){
            jObject.put("ProjectInfo", projectInfoReturn);
            jObject.put("Message", "success");
        }
        else{
            jObject.put("Message", "error");
        }

        return jObject;
    }

}
