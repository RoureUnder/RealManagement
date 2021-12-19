package com.group.realmanagement.repository.Projects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.JsonTest;
import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.entity.Projects.ProjectInfoReturn;
import com.group.realmanagement.repository.User.GuestRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

@SpringBootTest
public class ProjectInfoRepositoryTest {
    @Autowired
    private ProjectInfoRepository projectInfoRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private GuestRepository guestRepository;


    // @Test
    // void findAll(){
    //     System.out.println(projectInfoRepository.findAll());
    // }

    @Test
    void findAll(){
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
        System.out.println(projectInfoReturns);
    }

    @Test
    void findByProjectNo(){
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(10001);
        ProjectInfoReturn projectInfoReturn = new ProjectInfoReturn();
        projectInfoReturn.setProjectDetail(projectInfo,staffRepository,guestRepository);
        System.out.println(projectInfoReturn);
    }

    @Test
    void test(){
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(10001);
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
                    File dest = new File(fixedPath+ tempPath);
                    dest.mkdirs();
                    System.out.println(fixedPath+ tempPath);
                    tempPath=jObject.getJSONArray("dirs").getJSONObject(i).getString("name");
                }
            }
            else{
                File dest = new File(fixedPath+ tempPath);
                dest.mkdirs();
                System.out.println(fixedPath+tempPath);
            }
        }
        // System.out.println(jObject.getJSONArray("dirs").getJSONObject(1).size());
    }

    @Test
    void deleteDirectory(){
        File dest = new File("E:/房地产项目/测试项目名-张三-一级单位1-2021-12-17/模型");
        System.out.println(FileSystemUtils.deleteRecursively(dest));
    }
}
