package com.group.realmanagement.controller.User;

import java.util.Base64;
import java.util.List;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.alibaba.fastjson.JSONObject;
import com.group.realmanagement.entity.User.User;
import com.group.realmanagement.repository.User.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserHandler {
    @Autowired
    private UserRepository userRepository;

    // @Autowired
    // private StaffRepository staffRepository;
    @GetMapping("/findAll")
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @GetMapping("/findByUsername")
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @GetMapping("/findByStaffNo")
    public JSONObject findByStaffNo(int staffNo) throws Exception{
        JSONObject jObject = new JSONObject();
        User user = userRepository.findByStaffNo(staffNo);
        if(user==null){
            jObject.put("Result", "error");
        }
        else{
            jObject.put("User", user);
            jObject.put("Result", "success");
        }
        // user.setPassword(new String(decryptBASE64(user.getPassword())));
        return jObject;
    }

    @PostMapping("/register")
    public JSONObject save(@RequestBody User user){
        JSONObject jsonObject = new JSONObject();
        if(userRepository.findByUsername(user.getUsername())!=null){
            jsonObject.put("Result", "error");
            jsonObject.put("Message", "该账户已被注册");
        }
        else{
            user.setPassword(encryptBASE64(user.getPassword().getBytes()));//加密后存入数据库
            User user2 = userRepository.save(user);
            if(user2 != null)
            {
                jsonObject.put("Result", "success");
            }
            else jsonObject.put("Result", "success");
            }
        return jsonObject;
    }

    @PutMapping("/update")
    public String update(@RequestBody User user){

        int res = userRepository.updateByStaffNo(user.getUsername(), encryptBASE64(user.getPassword().getBytes()), user.getAccess(), user.getStaffNo());

        if(res==1)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }


    @DeleteMapping("/deleteByStaffNo")
    public String delete(int staffNo){
        int res = userRepository.deleteByStaffNo(staffNo);
        if(res==1)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }

    @PostMapping("/login")
    public JSONObject login(String userName,String password){
        password = encryptBASE64(password.getBytes());//加密密码
        User user = userRepository.findByUsername(userName);

        JSONObject jObject = new JSONObject();
        if(user != null&&user.getPassword().equals(password))
        {
            jObject.put("Result", "success");
            jObject.put("User", user);
        }
        else{
            jObject.put("Result", "error");
            jObject.put("Message", "用户名或密码错误");
        }
        return jObject;
    }





    // user.setPassword(encryptBASE64(password.getBytes()));//加密返回
     // user.setPassword(new String(decryptBASE64(user.getPassword())));//解密

    /**
     * BASE64Encoder 加密
     *
     * @param data
     *            要加密的数据
     * @return 加密后的字符串
     */
    public static String encryptBASE64(byte[] data) {
        // BASE64Encoder encoder = new BASE64Encoder();
        // String encode = encoder.encode(data);
        // 从JKD 9开始rt.jar包已废除，从JDK 1.8开始使用java.util.Base64.Encoder
        Encoder encoder = Base64.getEncoder();
        String encode = encoder.encodeToString(data);
        return encode;
    }
    /**
     * BASE64Decoder 解密
     *
     * @param data
     *            要解密的字符串
     * @return 解密后的byte[]
     * @throws Exception
     */
    public static byte[] decryptBASE64(String data) throws Exception {
        // BASE64Decoder decoder = new BASE64Decoder();
        // byte[] buffer = decoder.decodeBuffer(data);
        // 从JKD 9开始rt.jar包已废除，从JDK 1.8开始使用java.util.Base64.Decoder
        Decoder decoder = Base64.getDecoder();
        byte[] buffer = decoder.decode(data);
        return buffer;
    }
}
