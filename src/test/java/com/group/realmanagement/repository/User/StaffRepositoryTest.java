package com.group.realmanagement.repository.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StaffRepositoryTest {
    @Autowired
    private StaffRepository staffRepository;
    @Test
    void findAll(){
        System.out.println(staffRepository.findAll());
    }

    @Test
    void deleteByStaffNo(){
        System.out.println(staffRepository.deleteByStaffNo(10004)); 
    }

    @Test
    void updateByStaffNo(){
        System.out.println(staffRepository.updateByStaffNo("name", "department", "post", "contact", 9999, 99999, 10003));
    }

    @Test
    void findByStaffName(){
        System.out.println(staffRepository.findByStaffName("人员"));
    }

    @Test
    void findByStaffNo(){
        System.out.println(staffRepository.findByStaffNo(10001));
    }
}
