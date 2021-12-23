package com.group.realmanagement.controller.Projects;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.ProjectFileReview;
import com.group.realmanagement.repository.Projects.ProjectFileReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/project/file/review")
public class ProjectFileReviewHandler {
    @Autowired
    private ProjectFileReviewRepository projectFileReviewRepository;

    @GetMapping("/findAll")
    public JSONObject getMethodName() {
        JSONObject jObject = new JSONObject();
        List<ProjectFileReview> projectFileReviews = projectFileReviewRepository.findAll();
        if(projectFileReviews.size()!=0){
            jObject.put("ProjectFileReviews", projectFileReviews);
            jObject.put("Result", "success");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "当前没有待审核文件");
        }
        return jObject;
    }
}
