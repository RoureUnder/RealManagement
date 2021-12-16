package com.group.realmanagement.repository.FileServices;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProjectFileRepositoryTest {
    @Autowired
    private ProjectFileRepository projectFileRepository;

    @Test
    void testFile(){
        System.out.println(projectFileRepository.findAll());
    }
}
