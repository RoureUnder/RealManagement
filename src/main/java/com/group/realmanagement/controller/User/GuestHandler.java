package com.group.realmanagement.controller.User;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.User.Guest;
import com.group.realmanagement.repository.User.GuestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guest")
public class GuestHandler {
    @Autowired
    private GuestRepository guestRepository;

    @GetMapping("/findAll")
    List<Guest> findAll(){
        return guestRepository.findAll();
    }

    @GetMapping("/findByGuestName")
    List<Guest> findByGuestName(String guestName){
        return guestRepository.findByGuestName(guestName);
    }

    @GetMapping("/findByGuestNo")
    JSONObject findByGuestNo(int guestNo){
        JSONObject jObject = new JSONObject();
        Guest guest = guestRepository.findByGuestNo(guestNo);
        if(guest != null){
            jObject.put("Guest", guest);
            jObject.put("Result", "success");
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "未找到该员工，请检查后重试");
        }
        return jObject;
    }

    @PostMapping("/create")
    public String save(@RequestBody Guest guest){
        Guest guest2 = guestRepository.save(guest);
        if(guest2 != null)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }

    @DeleteMapping("/deleteByGuestNo")
    public String delete(int guestNo){
        int res = guestRepository.deleteByGuestNo(guestNo);
        if(res==1)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }

    @PutMapping("/updateByGuestNo")
    public String update(@RequestBody Guest guest){
        int res = guestRepository.updateByGuestNo(guest.getGuestName(), guest.getPrimaryUnit(), guest.getSecondaryUnit(), guest.getTertiaryUnit(), guest.getTelephone(), guest.getMobilePhone(), guest.getQq(), guest.getEmail(), guest.getGuestNo());
        if(res==1)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }
    //三级级联查询未实现
}
