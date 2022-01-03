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
            jObject.put("Message", "未找到员工，请检查后重试");
        }
        return jObject;
    }

    @GetMapping("/findByStaffNo")
    public JSONObject findByStaffNo(int staffNo){
        JSONObject jObject = new JSONObject();
        Staff staff = staffRepository.findByStaffNo(staffNo);
        if(staff!=null){
            jObject.put("Result", "success");
            jObject.put("Staff", staff);
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "未找到员工，请检查后重试");
        }
        return jObject;
    }

    @GetMapping("/findDepartmentByStaffNo")
    public JSONObject findDepartmentByStaffNo(int staffNo){
        JSONObject jObject = new JSONObject();
        Staff staff = staffRepository.findByStaffNo(staffNo);
        if(staff == null){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到员工信息");
            return jObject;
        }
        List<Staff> staffs = staffRepository.findByDepartmentName(staff.getDepartment());
        if(staffs.size()==0){
            jObject.put("Result", "error");
            jObject.put("Message", "该部门下无员工");
            return jObject;
        }
        jObject.put("Staffs", staffs);
        jObject.put("Result", "success");
        return jObject;
    }

    @GetMapping("/findPrincipal")
    public JSONObject findPrincipal(){
        JSONObject jObject = new JSONObject();
        List<Staff> staffs = staffRepository.findAll();
        List<Staff> staffs2 = new ArrayList<>();
        for (Staff staff : staffs) {
            if(staff.getPost().equals("模型主管")){
                staffs2.add(staff);
            }
        }
        if(staffs.size()==0){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到模型主管");
            return jObject;
        }
        jObject.put("Staffs", staffs2);
        jObject.put("Result", "success");
        return jObject;
    }

    @GetMapping("/findModelPrincipal")
    public JSONObject findModelPrincipal(){
        JSONObject jObject = new JSONObject();
        List<Staff> staffs = staffRepository.findAll();
        List<Staff> staffs2 = new ArrayList<>();
        for (Staff staff : staffs) {
            if(staff.getPost().equals("模型主管")){
                staffs2.add(staff);
            }
        }
        if(staffs.size()==0){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到模型主管");
            return jObject;
        }
        jObject.put("Staffs", staffs2);
        jObject.put("Result", "success");
        return jObject;
    }

    @GetMapping("/findRenderPrincipal")
    public JSONObject findRenderPrincipal(){
        JSONObject jObject = new JSONObject();
        List<Staff> staffs = staffRepository.findAll();
        List<Staff> staffs2 = new ArrayList<>();
        for (Staff staff : staffs) {
            if(staff.getPost().equals("渲染主管")){
                staffs2.add(staff);
            }
        }
        if(staffs.size()==0){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到渲染主管");
            return jObject;
        }
        jObject.put("Staffs", staffs2);
        jObject.put("Result", "success");
        return jObject;
    }

    @GetMapping("/findLatePrincipal")
    public JSONObject findLatePrincipal(){
        JSONObject jObject = new JSONObject();
        List<Staff> staffs = staffRepository.findAll();
        List<Staff> staffs2 = new ArrayList<>();
        for (Staff staff : staffs) {
            if(staff.getPost().equals("后期主管")){
                staffs2.add(staff);
            }
        }
        if(staffs.size()==0){
            jObject.put("Result", "error");
            jObject.put("Message", "未找到后期主管");
            return jObject;
        }
        jObject.put("Staffs", staffs2);
        jObject.put("Result", "success");
        return jObject;
    }

    @PostMapping("/create")
    JSONObject save(@RequestBody Staff staff){
        JSONObject jObject = new JSONObject();
        staff.setStatus(0);
        Staff staff2 = staffRepository.findByStaffNo(staff.getStaffNo());
        if(staff2==null){
            staff = staffRepository.save(staff);
            jObject.put("Staff", staff);
            jObject.put("Result", "success");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "该员工编号已被使用");
        }
        return jObject;
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
            staff.getAccess(),0,staff.getStaffNo());
        if(res==1)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }

}
