package com.group.realmanagement.controller.User;

import java.util.List;

import com.group.realmanagement.entity.User.Staff;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
public class StaffHandler {
    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/findAll")
    public List<Staff> findAll(){
        return staffRepository.findAll();
    }
}
