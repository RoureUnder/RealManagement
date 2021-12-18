package com.group.realmanagement.controller.User;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.User.Staff;
import com.group.realmanagement.repository.User.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/findByStaffName")
    public JSONObject findByStaffName(String staffName){
        JSONObject jObject = new JSONObject();
        List<Staff> staff = staffRepository.findByStaffName(staffName);
        if(staff.size()!=0){
            jObject.put("Staffs", staff);
            jObject.put("Result", "success");
        }
        else{
            jObject.put("Result", "error");
        }
        return jObject;
    }

    @PostMapping("/create")
    public String save(@RequestBody Staff staff){
        Staff staff2 = staffRepository.save(staff);
        if(staff2 != null)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }

    @DeleteMapping("/deleteByStaffNo")
    public String delete(int staffNo){
        int res = staffRepository.deleteByStaffNo(staffNo);
        if(res==1)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }

    @PutMapping("/updateByStaffNo")
    public String update(@RequestBody Staff staff){
        // staff.setPassword();//加密后存入数据库
        int res = staffRepository.updateByStaffNo(staff.getName(),staff.getDepartment(),staff.getPost(),staff.getContact(),
            staff.getAccess(),staff.getStatus(),staff.getStaffNo());
        if(res==1)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }
}
