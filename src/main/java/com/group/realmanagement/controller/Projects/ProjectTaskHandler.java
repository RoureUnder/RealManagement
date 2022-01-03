package com.group.realmanagement.controller.Projects;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.ProjectFile;
import com.group.realmanagement.entity.Projects.ProjectFileReview;
import com.group.realmanagement.entity.Projects.ProjectTask;
import com.group.realmanagement.entity.Projects.ProjectTaskReturn;
import com.group.realmanagement.repository.Projects.ProjectFileRepository;
import com.group.realmanagement.repository.Projects.ProjectFileReviewRepository;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.Projects.ProjectTaskRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/project/task")
public class ProjectTaskHandler {
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private ProjectInfoRepository projectInfoRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ProjectFileReviewRepository projectFileReviewRepository;

    @GetMapping("/findAll")
    public JSONObject findAll(){
        JSONObject jObject = new JSONObject();
        List<ProjectTask> projectTasks = projectTaskRepository.findAll();
        List<ProjectTaskReturn> projectTaskReturns = new ArrayList<>();
        for (ProjectTask projectTask : projectTasks) {
            ProjectTaskReturn tempProjectTaskReturn = new ProjectTaskReturn(projectTask, projectInfoRepository, staffRepository);
            projectTaskReturns.add(tempProjectTaskReturn);
        }
        if(projectTaskReturns.size() !=0){
            jObject.put("ProjectTaskReturns", projectTaskReturns);
            jObject.put("Result", "success");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "未找到分配的任务，请检查后重试");
        }
        return jObject;
    }

    @GetMapping("/findBySenderNo")
    public JSONObject findBySenderNo(int senderNo){
        JSONObject jObject = new JSONObject();
        List<ProjectTask> projectTasks = projectTaskRepository.findBySenderNo(senderNo);
        List<ProjectTaskReturn> projectTaskReturns = new ArrayList<>();
        for (ProjectTask projectTask : projectTasks) {
            ProjectTaskReturn tempProjectTaskReturn = new ProjectTaskReturn(projectTask, projectInfoRepository, staffRepository);
            projectTaskReturns.add(tempProjectTaskReturn);
        }
        if(projectTaskReturns.size() !=0){
            jObject.put("ProjectTaskReturns", projectTaskReturns);
            jObject.put("Result", "success");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该主管分配的任务，请检查后重试");
        }
        return jObject;
    }

    @GetMapping("/findByReceiverNo")
    public JSONObject findByReceiverNo(int receiverNo){
        JSONObject jObject = new JSONObject();
        List<ProjectTask> projectTasks = projectTaskRepository.findByReceiverNo(receiverNo);
        List<ProjectTaskReturn> projectTaskReturns = new ArrayList<>();
        for (ProjectTask projectTask : projectTasks) {
            ProjectTaskReturn tempProjectTaskReturn = new ProjectTaskReturn(projectTask, projectInfoRepository, staffRepository);
            projectTaskReturns.add(tempProjectTaskReturn);
        }
        if(projectTaskReturns.size() !=0){
            jObject.put("ProjectTasks", projectTaskReturns);
            jObject.put("Result", "success");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该员工被分配的任务，请检查后重试");
        }
        return jObject;
    }

    @GetMapping("/findByProjectNoAndSenderNo")
    public JSONObject findByProjectNoAndSenderNo(int projectNo,int senderNo){
        JSONObject jObject = new JSONObject();
        List<ProjectTask> projectTasks = projectTaskRepository.findByProjectNoAndSenderNo(projectNo, senderNo);
        List<ProjectTaskReturn> projectTaskReturns = new ArrayList<>();
        for (ProjectTask projectTask : projectTasks) {
            ProjectTaskReturn tempProjectTaskReturn = new ProjectTaskReturn(projectTask, projectInfoRepository, staffRepository);
            projectTaskReturns.add(tempProjectTaskReturn);
        }
        if(projectTaskReturns.size() !=0){
            jObject.put("ProjectTasks", projectTaskReturns);
            jObject.put("Result", "success");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该项目该主管分配的任务，请检查后重试");
        }
        return jObject;
    }

    @GetMapping("/checkAllTasksFinished")
    public JSONObject checkAllTasksFinished(int projectNo,int senderNo){
        //查看某项目下某主管发布的任务是否全部完成
        JSONObject jObject = new JSONObject();
        List<ProjectTask> projectTasks = projectTaskRepository.findByProjectNoAndSenderNo(projectNo,senderNo);
        List<ProjectTask> unfinishedProjectTasks = new ArrayList<>();
        for (ProjectTask projectTask : projectTasks) {
            if(projectTask.getFinished()!=1){
                unfinishedProjectTasks.add(projectTask);
            }
        }
        if(unfinishedProjectTasks.size() !=0){
            jObject.put("UnfinishedProjectTasks", unfinishedProjectTasks);
            jObject.put("Result", "error");
            jObject.put("Message", "该项目还有未完成任务");
        }
        else{
            jObject.put("ProjectTasks", projectTasks);
            jObject.put("Result", "success");
        }
        return jObject;
    }

    @PutMapping("/submit")//提交任务
    public JSONObject submitTask(@RequestBody ProjectTask projectTask){
        JSONObject jObject = new JSONObject();
        projectTask.setProcessTime(new Date());
        projectTask.setStatus(1);
        projectTask.setFinished(1);
        projectTask.setProjectFileNo(0);
        projectTask =  projectTaskRepository.save(projectTask);
        jObject.put("ProjectTask", projectTask);
        jObject.put("Result", "success");
        return jObject;
    }

    @PostMapping("/assign")//分配任务
    public JSONObject assignTask(@RequestBody ProjectTask projectTask){
        JSONObject jObject = new JSONObject();
        projectTask.setSendTime(new Date());
        projectTask.setStatus(0);
        projectTask.setReason("");
        projectTask.setFinished(0);
        projectTask.setProjectFileNo(0);
        projectTask =  projectTaskRepository.save(projectTask);
        jObject.put("ProjectTask", projectTask);
        jObject.put("Result", "success");
        return jObject;
    }

    @DeleteMapping("/delete")//删除任务+文件
    public JSONObject deleteTask(int taskNo){
        JSONObject jObject = new JSONObject();
        //删除本地文件project_file+project_file_review
        Optional<ProjectTask> projectTask = projectTaskRepository.findById(taskNo);
        if(projectTask.isEmpty()){
            jObject.put("Message", "删除任务失败");
            jObject.put("Result", "error");
            return jObject;
        }

        Optional<ProjectFile> projectFile = projectFileRepository.findById(projectTask.get().getProjectFileNo());
        ProjectFileReview projectFileReview = projectFileReviewRepository.findByTaskNo(projectTask.get().getNo());
        if(projectFile.isEmpty()){
            if(projectTask.get().getProjectNo()==0){
                projectTaskRepository.delete(projectTask.get());
                jObject.put("Message", "该任务不含文件，成功删除");
                 jObject.put("Result", "success");
                 return jObject;
            }
            jObject.put("Message", "对应文件不存在");
            jObject.put("Result", "error");
            return jObject;
        }
        else{//有对应project_file和project_file_review
            File file = new File(projectFile.get().getFullPath());
            if (!file.exists()) {
                jObject.put("Result", "error");
                jObject.put("Message", "文件不存在");
                return jObject;
            } else {
                if (file.delete()) {
                    projectFileReviewRepository.delete(projectFileReview);
                    projectFileRepository.delete(projectFile.get());
                    projectTaskRepository.delete(projectTask.get());
                    jObject.put("Result", "success");
                    jObject.put("删除的projectTask", projectTask.get());
                    jObject.put("删除的projectFile", projectFile.get());
                    jObject.put("删除的projectFileReview", projectFileReview);
                    jObject.put("Message", "本地删除成功，服务器映射删除");
                    return jObject;
                } else {
                    jObject.put("Result", "error");
                    jObject.put("Message", "任务删除失败。原因:本地文件删除失败");
                    return jObject;
                }
            }
        }
    }

    @PutMapping("/update")
    public JSONObject updateTask(@RequestBody ProjectTaskReturn projectTaskReturn){
        JSONObject jObject = new JSONObject();
        ProjectTask projectTask = new ProjectTask(projectTaskReturn);
        projectTask.setStatus(0);
        projectTask.setReason("");
        projectTask =  projectTaskRepository.save(projectTask);
        jObject.put("ProjectTask", projectTask);
        jObject.put("Result", "success");
        return jObject;
    }

    @PutMapping("/accept")//接受任务
    public JSONObject acceptTask(@RequestBody ProjectTask projectTask){
        JSONObject jObject = new JSONObject();
        projectTask.setProcessTime(new Date());
        projectTask.setStatus(1);
        projectTask.setFinished(0);
        projectTask.setReason("任务已接受");
        projectTask =  projectTaskRepository.save(projectTask);
        jObject.put("ProjectTask", projectTask);
        jObject.put("Result", "success");
        return jObject;
    }

    @PutMapping("/reject")
    public JSONObject rejectTask(@RequestBody ProjectTask projectTask){
        JSONObject jObject = new JSONObject();
        projectTask.setProcessTime(new Date());
        projectTask.setStatus(2);
        projectTask.setFinished(0);
        projectTask =  projectTaskRepository.save(projectTask);
        jObject.put("ProjectTask", projectTask);
        jObject.put("Result", "success");
        return jObject;
    }
}
