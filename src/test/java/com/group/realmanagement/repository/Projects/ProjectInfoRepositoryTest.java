package com.group.realmanagement.repository.Projects;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ProjectInfoRepositoryTest {
    @Autowired
    private ProjectInfoRepository projectInfoRepository;



    @Test
    void findAll(){
        System.out.println(projectInfoRepository.findAll());
    }
}
