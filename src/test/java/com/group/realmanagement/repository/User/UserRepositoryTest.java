package com.group.realmanagement.repository.User;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findAll(){
        System.out.println(userRepository.findAll());
    }

    @Test
    void fintByStaffNo(){
        System.out.println(userRepository.findByStaffNo(568));
    }


    @Test
    void updateByStaffNo(){
        System.out.println(userRepository.updateByStaffNo("username", "password", 312423, 0)); 
    }

    @Test
    void deleteByStaffNo(){
        System.out.println(userRepository.deleteByStaffNo(0));
    }
}
