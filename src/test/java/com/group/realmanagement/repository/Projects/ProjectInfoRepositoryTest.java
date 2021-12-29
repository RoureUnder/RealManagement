package com.group.realmanagement.repository.Projects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.Projects.JsonTest;
import com.group.realmanagement.entity.Projects.ProjectInfo;
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


    @Test
    void findAll(){
        System.out.println(projectInfoRepository.findAll());
    }
}
