package com.group.realmanagement.controller.Projects;

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
@RequestMapping("/project")
public class ProjectHandler {
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

    @PostMapping("/initializeByProjectNo")
    JSONObject initializeByProjectNo(int projectNo){
        /*
        * func:初始化项目结构
        * 步骤：
        * 1.从project_info表中查询对应的项目信息
        * 2.本地建立目录结构
        * 3.数据库project_file建立映射
        */
        JSONObject rObject = new JSONObject();

        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);

        if(projectInfo == null){
            rObject.put("Result", "error");
            rObject.put("Message", "项目编号错误");
            return rObject;
        }
        ProjectInfoReturn projectInfoReturn = new ProjectInfoReturn();
        projectInfoReturn.setProjectDetail(projectInfo,staffRepository,guestRepository);
        String fixedPath="E:/房地产项目/";
        fixedPath+=projectInfoReturn.getProjectName()+"-"
                +projectInfoReturn.getGuestName()+"-"
                +guestRepository.findByGuestNo(projectInfo.getGuestNo()).getPrimaryUnit()+"-"
                +projectInfoReturn.getStartTime()+"/";
        System.out.println(fixedPath);

        JSONObject jObject = new JSONObject();
        String path = "D:/VsCodeProjects/VS-Code-Springboot/RealManagement/realmanagement/src/main/resources/depends/Directory.json";
        String str = JsonTest.readJsonFile(path);
        jObject = JSON.parseObject(str);
        for(int i=0;i<5;i++){
            String tempPath=jObject.getJSONArray("dirs").getJSONObject(i).getString("name");
            if(jObject.getJSONArray("dirs").getJSONObject(i).size()!=1)
            {
                for(int j=0;j<2;j++){
                    tempPath+="/"+jObject.getJSONArray("dirs").getJSONObject(i).getJSONArray("dirs").getJSONObject(j).getString("name");
                    System.out.println(fixedPath+ tempPath);
                    tempPath=jObject.getJSONArray("dirs").getJSONObject(i).getString("name");
                }
            }
            else{
                System.out.println(fixedPath+tempPath);
            }
        }

        rObject.put("result", "success");
        return rObject;

    }
}
