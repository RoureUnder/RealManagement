package com.group.realmanagement.controller.Projects;

import java.io.File;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.JsonTest;
import com.group.realmanagement.entity.Projects.ProjectFile;
import com.group.realmanagement.entity.Projects.ProjectFileReview;
import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.entity.Projects.ProjectInfoReturn;
import com.group.realmanagement.entity.Projects.ProjectTask;
import com.group.realmanagement.entity.User.Guest;
import com.group.realmanagement.entity.User.Staff;

import com.group.realmanagement.repository.Projects.ProjectFileRepository;
import com.group.realmanagement.repository.Projects.ProjectFileReviewRepository;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.Projects.ProjectTaskRepository;
import com.group.realmanagement.repository.User.GuestRepository;
import com.group.realmanagement.repository.User.StaffRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project/info")
public class ProjectInfoHandler {
    @Autowired
    private ProjectInfoRepository projectInfoRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private ProjectFileReviewRepository projectFileReviewRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private GuestRepository guestRepository;

    @GetMapping("/findAllOrigin")
    JSONObject findAllOrigin() {
        JSONObject jObject = new JSONObject();
        List<ProjectInfo> projectInfos = projectInfoRepository.findAll();
        if (projectInfos.size() != 0) {
            jObject.put("ProjectInfos", projectInfos);
            jObject.put("Result", "success");
        } else {
            jObject.put("Result", "error");
        }
        return jObject;
    }

    @GetMapping("/findAll")
    JSONObject findAll() {
        JSONObject jObject = new JSONObject();
        List<ProjectInfo> projectInfos = projectInfoRepository.findAll();
        List<ProjectInfoReturn> projectInfoReturns = new ArrayList<>();
        if (projectInfos.size() != 0) {
            for (ProjectInfo projectInfo : projectInfos) {
                ProjectInfoReturn tempProjectInfoReturn = new ProjectInfoReturn(projectInfo, staffRepository, guestRepository);
                projectInfoReturns.add(tempProjectInfoReturn);
            }
            jObject.put("ProjectInfoReturns", projectInfoReturns);
            jObject.put("Result", "success");
        } else {
            jObject.put("Result", "error");
        }
        return jObject;
    }

    @GetMapping("/findByGuestName")
    JSONObject findByGuestName(String guestName) {
        JSONObject jObject = new JSONObject();
        List<ProjectInfo> projectInfos = projectInfoRepository.findAll();
        List<ProjectInfoReturn> projectInfoReturns = new ArrayList<>();
        if (projectInfos.size() != 0) {
            for (ProjectInfo projectInfo : projectInfos) {
                ProjectInfoReturn tempProjectInfoReturn = new ProjectInfoReturn(projectInfo, staffRepository, guestRepository);
                if(tempProjectInfoReturn.getGuestName().indexOf(guestName)!=-1){
                    projectInfoReturns.add(tempProjectInfoReturn);
                }
            }
            if (projectInfoReturns.size() != 0) {
                jObject.put("ProjectInfoReturns", projectInfoReturns);
                jObject.put("Result", "success");
            }
            else{
                jObject.put("Result", "error");
                jObject.put("Message", "未找到项目信息");
            }
        }
        else {
            jObject.put("Result", "error");
            jObject.put("Message", "未找到项目信息");
        }
        return jObject;
    }

    @GetMapping("/findByStartTime")
    JSONObject findByStartTime(String startTime) {
        JSONObject jObject = new JSONObject();
        List<ProjectInfo> projectInfos = projectInfoRepository.findAll();
        List<ProjectInfoReturn> projectInfoReturns = new ArrayList<>();
        if (projectInfos.size() != 0) {
            for (ProjectInfo projectInfo : projectInfos) {
                ProjectInfoReturn tempProjectInfoReturn = new ProjectInfoReturn(projectInfo, staffRepository, guestRepository);
                if(tempProjectInfoReturn.getStartTime().toString().indexOf(startTime)!=-1){
                    projectInfoReturns.add(tempProjectInfoReturn);
                }
            }
            if (projectInfoReturns.size() != 0) {
                jObject.put("ProjectInfoReturns", projectInfoReturns);
                jObject.put("Result", "success");
            }
            else{
                jObject.put("Result", "error");
                jObject.put("Message", "未找到项目信息");
            }
        }
        else {
            jObject.put("Result", "error");
            jObject.put("Message", "未找到项目信息");
        }
        return jObject;
    }

    @GetMapping("/findByProjectNo")
    JSONObject findByProjectNo(int projectNo) {
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);
        if (projectInfo != null) {
            ProjectInfoReturn projectInfoReturn = new ProjectInfoReturn(projectInfo, staffRepository, guestRepository);
            jObject.put("ProjectInfoReturn", projectInfoReturn);
            jObject.put("Result", "success");
        } else {
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该项目，请联系管理员");
        }
        return jObject;
    }

    @GetMapping("/findByPrincipalNo")
    JSONObject findByPrincipalNo(int principalNo) {
        JSONObject jObject = new JSONObject();
        Staff staff = staffRepository.findByStaffNo(principalNo);
        if(staff == null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该主管");
            return jObject;
        }
        List<ProjectInfo> projectInfos = new ArrayList<>();
        projectInfos = projectInfoRepository.findByModelNo(principalNo);
        projectInfos.addAll(projectInfoRepository.findByRenderNo(principalNo));
        projectInfos.addAll(projectInfoRepository.findByLateNo(principalNo));
        projectInfos.addAll(projectInfoRepository.findByPrincipalNo(principalNo));
        int flag=0;
        for (int i =0;i<projectInfos.size();i++) {
            for(int j =i+1;j<projectInfos.size();j++){
                if(projectInfos.get(i).equals(projectInfos.get(j))){
                    projectInfos.remove(projectInfos.get(j));
                    j--;
                    flag++;
                }
            }
        }
        jObject.put("flag", flag);
        List<ProjectInfoReturn> projectInfoReturns = new ArrayList<>();
        for (ProjectInfo projectInfo : projectInfos) {
            ProjectInfoReturn tempProjectInfoReturn = new ProjectInfoReturn(projectInfo,staffRepository,guestRepository);
            projectInfoReturns.add(tempProjectInfoReturn);
        }
        if (projectInfoReturns.size() != 0) {
            jObject.put("ProjectInfoReturns", projectInfoReturns);
            jObject.put("Result", "success");
        } else {
            jObject.put("Result", "error");
            jObject.put("Message", "该主管项目为空");
        }
        return jObject;
    }

    @PostMapping("/create")
    JSONObject create(@RequestBody ProjectInfo projectInfo) {
        /*
        *   project_info表中记录项目资料
        *   创建本地目录
        */
        JSONObject jObject = new JSONObject();
        projectInfo.setProjectNo(generateProjectNo());

        //验证客户信息和项目负责人Principle信息
        Guest guest = guestRepository.findByGuestNo(projectInfo.getGuestNo());
        Staff principal = staffRepository.findByStaffNo(projectInfo.getPrincipalNo());

        if(guest == null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到客户信息，请先添加客户资料");
            return jObject;
        }
        else if(principal == null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到主管信息，请检查后重试");
            return jObject;
        }
        /*
        *设置对应阶段状态
        *Status:    0   未开始
        *           1   进行中
        *           2   已完成
        */
        projectInfo.setModelStatus(0);

        projectInfo.setRenderStatus(0);

        projectInfo.setLateStatus(0);
        projectInfo.setStatus(0);
        // projectInfo.setStartTime(new Date());
        projectInfo = projectInfoRepository.save(projectInfo);

        jObject.put("ProjectInfo", projectInfo);
        jObject.put("Result", "success");

        return jObject;
    }

    @DeleteMapping("/delete")
    JSONObject delete(int projectNo) {
        // 1.删掉服务器该项目所有映射(包括project_info和project_file和project_file_review和project_task)
        // 2.删除project_info表中数据
        // 3.删掉本地文件
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);

        if(projectInfo==null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该项目，请检查后重试");
            return jObject;
        }
        // 1    删掉服务器该项目所有映射(包括project_file和project_file_review)
        List<ProjectFile> projectFiles = projectFileRepository.findByProjectNo(projectNo);
        List<ProjectFileReview> projectFileReviews = projectFileReviewRepository.findByProjectNo(projectNo);
        List<ProjectTask> projectTasks = projectTaskRepository.findByProjectNo(projectNo);
        JSONObject project_file = new JSONObject();
        JSONObject project_file_review = new JSONObject();
        JSONObject project_task = new JSONObject();
        project_file.put("映射数量", "共" + projectFiles.size() + "个");
        project_file.put("映射详情", projectFiles);
        project_file_review.put("映射数量", "共" + projectFileReviews.size() + "个");
        project_file_review.put("映射详情", projectFileReviews);
        project_task.put("任务数量", "共"+project_task.size()+"个");
        project_task.put("任务详情", projectTasks);
        jObject.put("project_file", project_file);
        jObject.put("project_file_review", project_file_review);
        jObject.put("project_task", project_task);
        for (ProjectFile projectFile : projectFiles) {
            projectFileRepository.delete(projectFile);
        }
        for (ProjectFileReview projectFileReview : projectFileReviews) {
            projectFileReviewRepository.delete(projectFileReview);
        }
        for (ProjectTask projectTask : projectTasks) {
            projectTaskRepository.delete(projectTask);
        }
        // 2    删除project_info表中数据
        projectInfoRepository.delete(projectInfo);
        // 3    删掉本地文件
        String fullPath = "E:/房地产项目/" + projectInfo.getProjectName() + "-"
                + guestRepository.findByGuestNo(projectInfo.getGuestNo()).getGuestName() + "-"
                + guestRepository.findByGuestNo(projectInfo.getGuestNo()).getPrimaryUnit() + "-"
                + ProjectFileHandler.getFormatDate(projectInfo.getStartTime());
        jObject.put("项目目录", fullPath);
        File dest = new File(fullPath);
        jObject.put("删除文件结果", FileSystemUtils.deleteRecursively(dest));
        jObject.put("Result", "success");
        return jObject;
    }

    @DeleteMapping("/rollback")
    JSONObject rollback(int projectNo,int destStage){//回滚项目 参数：项目编号、目标阶段
        JSONObject jObject = new JSONObject();
        //1.验证项目信息和当前阶段
        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);
        if(projectInfo==null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到项目信息");
            return jObject;
        }
        int stage = 0;
        if(projectInfo.getModelStatus()==2){
            stage = 1;
        }
        if(projectInfo.getRenderStatus()==2){
            stage = 2;
        }
        if(projectInfo.getLateStatus()==2){
            stage = 3;
        }
        /*  destStage回滚目标阶段(要求值为1、2、3，且目标阶段需小于等于当前完成阶段)
        *   1.回滚到模型阶段(删除除资料外的所有文件和任务)
        *   2.回滚到渲染阶段(删除除资料/模型外的所有文件和任务)
        *   3.回滚到后期阶段(删除除资料/模型/渲染外的所有文件和任务)
        */
        if((destStage!=1&&destStage!=2&&destStage!=3)||destStage>stage){
            jObject.put("Result", "error");
            jObject.put("ProjectInfo", projectInfo);
            jObject.put("Message", "目标阶段不符合要求  destStage:"+destStage+" stage="+stage);
            return jObject;
        }
        //进行项目回滚(查找所有的文件、审核和任务)
        /**
         * 文件
         *  资料(taskNo==0)/模型/渲染/后期                      =>删除数据库+本地文件
         * 审核
         *  成功上传的文件(project_file中一定是保存过的)        =>  仅删除数据库记录
         *  拒绝的文件(未在project_file中保存,但本地中有文件)   =>  删除数据库+本地文件
         * 任务
         *  空任务(projectFileNo=0)和已完成的任务               =>  删除数据库
         */
        //
        List<ProjectFile> projectFiles = projectFileRepository.findByProjectNo(projectInfo.getProjectNo());
        List<ProjectFileReview> projectFileReviews = projectFileReviewRepository.findByProjectNo(projectInfo.getProjectNo());
        List<ProjectTask> projectTasks = projectTaskRepository.findByProjectNo(projectInfo.getProjectNo());

        JSONObject project_file = new JSONObject();
        JSONObject project_file_review = new JSONObject();
        JSONObject project_task = new JSONObject();

        List<ProjectFile> projectFileReturns = new ArrayList<>();
        List<ProjectFileReview> projectFileReviewReturns = new ArrayList<>();
        List<ProjectTask> projectTaskReturns = new ArrayList<>();

        switch(destStage){
            case 1:{
                /*  回滚到模型阶段
                 *  projectFile
                 *      删除除资料/小样外的所有文件和数据库记录
                 *  projectFileReview
                 *      删除所有通过的审核记录+文件   未通过的记录
                 *  projectTask
                 *      删除所有任务
                 */
                for (ProjectFile projectFile : projectFiles) {
                    if(projectFile.getModule().equals("资料")||projectFile.getModule().equals("小样")){
                        continue;
                    }
                    File file = new File(projectFile.getFullPath());
                    if (!file.exists()) {
                        jObject.put("Result", "error");
                        jObject.put("Message", "文件不存在");
                        return jObject;
                    } else {
                        if (file.delete()) {
                            projectFileRepository.delete(projectFile);
                            projectFileReturns.add(projectFile);
                        } else {
                            jObject.put("Result", "error");
                            jObject.put("Message", "本地删除失败");
                            return jObject;
                        }
                    }
                }
                for (ProjectFileReview projectFileReview : projectFileReviews) {
                    if(projectFileReview.getModule().equals("资料")||projectFileReview.getModule().equals("小样")){
                        continue;
                    }
                    if(projectFileReview.getStatus()==2){
                        File file = new File(projectFileReview.getFullPath());
                        if (!file.exists()) {
                            jObject.put("Result", "error");
                            jObject.put("Message", "文件不存在");
                            return jObject;
                        } else {
                            if (file.delete()) {
                                projectFileReviewRepository.delete(projectFileReview);
                                projectFileReviewReturns.add(projectFileReview);
                            } else {
                                jObject.put("Result", "error");
                                jObject.put("Message", "本地删除失败");
                                return jObject;
                            }
                        }
                    }
                    else{
                        projectFileReviewRepository.delete(projectFileReview);
                        projectFileReviewReturns.add(projectFileReview);
                    }
                }
                for (ProjectTask projectTask : projectTasks) {
                    projectTaskRepository.delete(projectTask);
                    projectTaskReturns.add(projectTask);
                }
                //修改当前项目状态
                projectInfo.setModelStatus(1);
                projectInfo.setRenderStatus(0);
                projectInfo.setLateStatus(0);
                projectInfo.setStatus(1);
                break;
            }
            case 2:{
                /**
                 * 回滚到渲染阶段
                 *  projectFile
                 *      删除除资料/小样/模型外的所有文件+数据库记录
                 *  projectFileReview
                 *      删除除模型外的所有未通过的文件+数据库记录 除模型外的所有通过的数据库记录
                 *  projectTask
                 *      删除除模型外的所有任务(根据receiver_no的对应权限来判断    模型34 渲染56 后期78)
                 */
                for (ProjectFile projectFile : projectFiles) {
                    if(projectFile.getModule().equals("资料")||projectFile.getModule().equals("小样")||projectFile.getModule().equals("模型")){
                        continue;
                    }
                    File file = new File(projectFile.getFullPath());
                    if (!file.exists()) {
                        jObject.put("Result", "error");
                        jObject.put("Message", "文件不存在");
                        return jObject;
                    } else {
                        if (file.delete()) {
                            projectFileRepository.delete(projectFile);
                            projectFileReturns.add(projectFile);
                        } else {
                            jObject.put("Result", "error");
                            jObject.put("Message", "本地删除失败");
                            return jObject;
                        }
                    }
                }
                for (ProjectFileReview projectFileReview : projectFileReviews) {
                    if(projectFileReview.getModule().equals("资料")||projectFileReview.getModule().equals("小样")||projectFileReview.getModule().equals("模型")){
                        continue;
                    }
                    if(projectFileReview.getStatus()==2){
                        File file = new File(projectFileReview.getFullPath());
                        if (!file.exists()) {
                            jObject.put("Result", "error");
                            jObject.put("Message", "文件不存在");
                            return jObject;
                        } else {
                            if (file.delete()) {
                                projectFileReviewRepository.delete(projectFileReview);
                                projectFileReviewReturns.add(projectFileReview);
                            } else {
                                jObject.put("Result", "error");
                                jObject.put("Message", "本地删除失败");
                                return jObject;
                            }
                        }
                    }
                    else{
                        projectFileReviewRepository.delete(projectFileReview);
                        projectFileReviewReturns.add(projectFileReview);
                    }
                }
                for (ProjectTask projectTask : projectTasks) {
                    Staff staff = staffRepository.findByStaffNo(projectTask.getReceiverNo());
                    if(staff==null){
                        jObject.put("Result", "error");
                        jObject.put("Message", "projectTask对应员工信息"+projectTask.getReceiverNo()+"未找到");
                    }
                    //除模型外的
                    if(staff.getAccess()==3||staff.getAccess()==4){
                        continue;
                    }
                    projectTaskRepository.delete(projectTask);
                    projectTaskReturns.add(projectTask);
                }
                projectInfo.setModelStatus(2);
                projectInfo.setRenderStatus(1);
                projectInfo.setLateStatus(0);
                projectInfo.setStatus(1);
                break;
            }
            case 3:{
                /**
                 * 回滚到后期阶段
                 *  projectFile
                 *      删除后期的所有文件+数据库记录
                 *  projectFileReview
                 *      删除后期的所有未通过的文件+数据库记录 后期的所有通过的数据库记录
                 *  projectTask
                 *      删除后期的所有任务(根据receiver_no的对应权限来判断    模型34 渲染56 后期78)
                 */
                for (ProjectFile projectFile : projectFiles) {
                    if(projectFile.getModule().equals("后期")){
                        File file = new File(projectFile.getFullPath());
                        if (!file.exists()) {
                            jObject.put("Result", "error");
                            jObject.put("Message", "文件不存在");
                            return jObject;
                        } else {
                            if (file.delete()) {
                                projectFileRepository.delete(projectFile);
                                projectFileReturns.add(projectFile);
                            } else {
                                jObject.put("Result", "error");
                                jObject.put("Message", "本地删除失败");
                                return jObject;
                            }
                        }
                    }
                }
                for (ProjectFileReview projectFileReview : projectFileReviews) {
                    if(projectFileReview.getModule().equals("后期")){
                        if(projectFileReview.getStatus()==2){
                            File file = new File(projectFileReview.getFullPath());
                            if (!file.exists()) {
                                jObject.put("Result", "error");
                                jObject.put("Message", "文件不存在");
                                return jObject;
                            } else {
                                if (file.delete()) {
                                    projectFileReviewRepository.delete(projectFileReview);
                                    projectFileReviewReturns.add(projectFileReview);
                                } else {
                                    jObject.put("Result", "error");
                                    jObject.put("Message", "本地删除失败");
                                    return jObject;
                                }
                            }
                        }
                        else{
                            projectFileReviewRepository.delete(projectFileReview);
                            projectFileReviewReturns.add(projectFileReview);
                        }
                    }
                }
                for (ProjectTask projectTask : projectTasks) {
                    Staff staff = staffRepository.findByStaffNo(projectTask.getReceiverNo());
                    if(staff==null){
                        jObject.put("Result", "error");
                        jObject.put("Message", "projectTask对应员工信息"+projectTask.getReceiverNo()+"未找到");
                    }
                    //除模型外的
                    if(staff.getAccess()==7||staff.getAccess()==8){
                        projectTaskRepository.delete(projectTask);
                        projectTaskReturns.add(projectTask);
                    }
                }
                projectInfo.setModelStatus(2);
                projectInfo.setRenderStatus(2);
                projectInfo.setLateStatus(1);
                projectInfo.setStatus(1);
                break;
            }
            default:{
                jObject.put("Result", "error");
                jObject.put("Message", "destStage错误 值为："+destStage);
                return jObject;
            }
        }
        project_file.put("文件映射数量", "共" + projectFileReturns.size() + "个");
        project_file.put("文件映射详情", projectFileReturns);
        project_file_review.put("审核数量", "共" + projectFileReviewReturns.size() + "个");
        project_file_review.put("审核详情", projectFileReviewReturns);
        project_task.put("任务数量", "共" + projectTaskReturns.size() + "个");
        project_task.put("任务详情", projectTaskReturns);
        jObject.put("project_file", project_file);
        jObject.put("project_file_review", project_file_review);
        jObject.put("project_task", project_task);

        projectInfo = projectInfoRepository.save(projectInfo);
        jObject.put("修改后的ProjectInfo", projectInfo);

        jObject.put("Result", "success");
        return jObject;
    }

    @PutMapping("/publish")
    JSONObject publish(@RequestBody ProjectInfoReturn projectInfoReturn) {
        //发布项目
        //修改项目状态并生成本地文件
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = new ProjectInfo(projectInfoReturn);

        ProjectInfo originProjectInfo = projectInfoRepository.findByProjectNo(projectInfo.getProjectNo());
        projectInfo.setModelStatus(1);
        projectInfo.setStartTime(new Date());
        projectInfo.setStatus(1);
        projectInfo = projectInfoRepository.save(projectInfo);
        jObject.put("原ProjectInfo", originProjectInfo);
        jObject.put("修改后的ProjectInfo", projectInfoReturn);
        jObject.put("文件", initializeByProjectNo(projectInfo.getProjectNo()));
        jObject.put("Result", "success");
        return jObject;
    }

    @PutMapping("/finishProject")
    JSONObject finishProject(@RequestBody ProjectInfoReturn projectInfoReturn){
        //客户审核结束项目
        JSONObject jObject = new JSONObject();
        ProjectInfo projectInfo = new ProjectInfo(projectInfoReturn);
        ProjectInfo originProjectInfo = projectInfoRepository.findByProjectNo(projectInfo.getProjectNo());
        if(originProjectInfo==null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该项目");
            return jObject;
        }
        if(projectInfo.getModelStatus()!=2){
            jObject.put("Result", "error");
            jObject.put("Message", "建模阶段尚未完成，无法结束项目");
            return jObject;
        }
        if(projectInfo.getRenderStatus()!=2){
            jObject.put("Result", "error");
            jObject.put("Message", "渲染阶段尚未完成，无法结束项目");
            return jObject;
        }
        if(projectInfo.getLateStatus()!=2){
            jObject.put("Result", "error");
            jObject.put("Message", "后期阶段尚未完成，无法结束项目");
            return jObject;
        }
        projectInfo.setEndTime(new Date());
        projectInfo.setStatus(2);
        projectInfo = projectInfoRepository.save(projectInfo);
        jObject.put("原ProjectInfo", originProjectInfo);
        jObject.put("修改后的ProjectInfo", projectInfoReturn);
        jObject.put("文件", initializeByProjectNo(projectInfo.getProjectNo()));
        jObject.put("Result", "success");
        return jObject;
    }

    @PutMapping("/assignPrincipal")
    JSONObject assignPrincipal(@RequestBody ProjectInfo projectInfo) {
        JSONObject jObject = new JSONObject();
        projectInfoRepository.save(projectInfo);
        jObject.put("ProjectInfo", projectInfo);
        jObject.put("Result", "success");
        return jObject;
    }

    @PutMapping("/submitStage")
    JSONObject submitStage(@RequestBody ProjectInfo projectInfo) {
        //提交阶段进度
        //需验证所有任务是否完成
        JSONObject jObject = new JSONObject();
        projectInfoRepository.save(projectInfo);
        jObject.put("ProjectInfo", projectInfo);
        // jObject.put("staffNo", staffNo);
        jObject.put("Result", "success");
        return jObject;
    }

    @GetMapping("/generateProjectNo")
    int generateProjectNo(){
        int max = 0;
        List<ProjectInfo> projectInfos = projectInfoRepository.findAll();
        for (ProjectInfo projectInfo : projectInfos) {
            max = projectInfo.getProjectNo() > max ? projectInfo.getProjectNo() : max;
        }
        return max + 1;
    }

    public JSONObject initializeByProjectNo(int projectNo) {
        /*
         * func:初始化项目结构
         * 步骤：
         * 1.从project_info表中查询对应的项目信息
         * 2.本地建立目录结构
         */
        JSONObject rObject = new JSONObject();

        ProjectInfo projectInfo = projectInfoRepository.findByProjectNo(projectNo);

        if (projectInfo == null) {
            rObject.put("Result", "error");
            rObject.put("Message", "项目编号错误");
            return rObject;
        }

        String fixedPath = "E:/房地产项目/" + projectInfo.getProjectName() + "-"
                + guestRepository.findByGuestNo(projectInfo.getGuestNo()).getGuestName() + "-"
                + guestRepository.findByGuestNo(projectInfo.getGuestNo()).getPrimaryUnit() + "-"
                + ProjectFileHandler.getFormatDate(projectInfo.getStartTime());
        // System.out.println(fixedPath);

        JSONObject jObject = new JSONObject();
        String path = "D:/VsCodeProjects/VS-Code-Springboot/RealManagement/realmanagement/src/main/resources/depends/Directory.json";
        String str = JsonTest.readJsonFile(path);
        jObject = JSON.parseObject(str);
        for (int i = 0; i < 5; i++) {
            String tempPath = "/" + jObject.getJSONArray("dirs").getJSONObject(i).getString("name");
            if (jObject.getJSONArray("dirs").getJSONObject(i).size() != 1) {
                for (int j = 0; j < 2; j++) {
                    tempPath += "/" + jObject.getJSONArray("dirs").getJSONObject(i).getJSONArray("dirs")
                            .getJSONObject(j).getString("name");
                    File dest = new File(fixedPath + tempPath);
                    dest.mkdirs();
                    // System.out.println(fixedPath+ tempPath);
                    tempPath = "/" + jObject.getJSONArray("dirs").getJSONObject(i).getString("name");
                }
            } else {
                File dest = new File(fixedPath + tempPath);
                dest.mkdirs();
                // System.out.println(fixedPath+tempPath);
            }
        }

        rObject.put("Result", "success");
        rObject.put("ProjectPath", fixedPath);
        return rObject;
    }
}
