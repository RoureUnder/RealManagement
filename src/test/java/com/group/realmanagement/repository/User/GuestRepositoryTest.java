package com.group.realmanagement.repository.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GuestRepositoryTest {
    @Autowired
    private GuestRepository guestRepository;

    @Test
    void findAll(){
        System.out.println(guestRepository.findAll());
    }

    @Test
    void findByGuestName(){
        System.out.println(guestRepository.findByGuestName("王五"));
    }
    @Test
    void deleteByGuestNo(){
        System.out.println(guestRepository.deleteByGuestNo(10003));
    }
}
