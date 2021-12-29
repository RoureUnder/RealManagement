package com.group.realmanagement.controller.Projects;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.ProjectFile;
import com.group.realmanagement.entity.Projects.ProjectFileReview;
import com.group.realmanagement.entity.Projects.ProjectInfo;
import com.group.realmanagement.entity.Projects.ProjectTask;
import com.group.realmanagement.entity.User.Staff;
import com.group.realmanagement.repository.Projects.ProjectFileRepository;
import com.group.realmanagement.repository.Projects.ProjectFileReviewRepository;
import com.group.realmanagement.repository.Projects.ProjectInfoRepository;
import com.group.realmanagement.repository.Projects.ProjectTaskRepository;
import com.group.realmanagement.repository.User.StaffRepository;

import org.hibernate.engine.transaction.jta.platform.internal.JOnASJtaPlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/project/file/review")
public class ProjectFileReviewHandler {
    @Autowired
    private ProjectFileReviewRepository projectFileReviewRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private ProjectTaskRepository projectTaskRepository;
    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/findAll")
    public JSONObject findAll() {
        JSONObject jObject = new JSONObject();
        List<ProjectFileReview> projectFileReviews = projectFileReviewRepository.findAll();
        if (projectFileReviews.size() != 0) {
            jObject.put("ProjectFileReviews", projectFileReviews);
            jObject.put("Result", "success");
        } else {
            jObject.put("Result", "error");
            jObject.put("Message", "当前没有待审核文件");
        }
        return jObject;
    }

    @GetMapping("/findByPrincipalNo")
    public JSONObject findByPrincipalNo(int principalNo) {
        JSONObject jObject = new JSONObject();
        Staff principal = staffRepository.findByStaffNo(principalNo);
        if (principal == null) {
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该主管");
            return jObject;
        }
        List<ProjectFileReview> projectFileReviews = projectFileReviewRepository.findByPrincipalNo(principalNo);
        if (projectFileReviews.size() != 0) {
            jObject.put("ProjectFileReviews", projectFileReviews);
            jObject.put("Result", "success");
        } else {
            jObject.put("Result", "error");
            jObject.put("Message", "当前主管没有待审核文件");
        }
        return jObject;
    }

    @GetMapping("/findByStaffNo")
    public JSONObject findByStaffNo(int staffNo) {
        JSONObject jObject = new JSONObject();
        Staff staff = staffRepository.findByStaffNo(staffNo);
        if (staff == null) {
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该员工");
            return jObject;
        }
        List<ProjectFileReview> projectFileReviews = projectFileReviewRepository.findByUploader(staffNo);
        if (projectFileReviews.size() != 0) {
            jObject.put("ProjectFileReviews", projectFileReviews);
            jObject.put("Result", "success");
        } else {
            jObject.put("Result", "error");
            jObject.put("Message", "当前员工没有待审核文件");
        }
        return jObject;
    }

    @PutMapping("/passReview")
    public JSONObject passReview(@RequestBody ProjectFileReview projectFileReview) {
        JSONObject jObject = new JSONObject();
        Optional<ProjectTask> projectTask = projectTaskRepository.findById(projectFileReview.getTaskNo());
        if(projectTask.isEmpty()){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该任务，请联系管理员");
            return jObject;
        }
        projectFileReview.setStatus(1);
        projectFileReview.setSuggestion("通过审核");
        projectFileReview.setProcessTime(new Date());

        ProjectFile projectFile = new ProjectFile(projectFileReview);
        // 写入project_file新映射
        projectFile = projectFileRepository.save(projectFile);
        // 修改任务状态为已完成
        projectTask.get().setFinished(1);
        projectTask.get().setProjectFileNo(projectFile.getNo());
        ProjectTask projectTask2 = projectTaskRepository.save(projectTask.get());

        jObject.put("新增projectFile映射", projectFile);
        jObject.put("更改的审核", projectFileReview);
        jObject.put("对应任务", projectTask2);
        projectFileReviewRepository.save(projectFileReview);
        jObject.put("Result", "success");
        return jObject;
    }

    @PutMapping("/rejectReview")
    public JSONObject reject(@RequestBody ProjectFileReview projectFileReview) {
        JSONObject jObject = new JSONObject();
        projectFileReview.setStatus(2);
        projectFileReview.setProcessTime(new Date());
        jObject.put("更改的审核", projectFileReview);
        projectFileReviewRepository.save(projectFileReview);
        jObject.put("Result", "success");
        return jObject;
    }
}
