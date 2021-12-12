package com.group.realmanagement.repository.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StaffRepositoryTest {
    @Autowired
    private StaffRepository staffRepository;
    @Test
    void findAll()
    {
        System.out.println(staffRepository.findAll());
    }
}
