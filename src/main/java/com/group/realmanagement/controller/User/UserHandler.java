package com.group.realmanagement.controller.User;

import java.util.Base64;
import java.util.List;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import com.group.realmanagement.entity.User.User;
import com.group.realmanagement.repository.User.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/findById")
    public User findById(int id){
        return userRepository.findById(id).get();
    }

    @GetMapping("/findByUsername")
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @GetMapping("/findByStaffNo")
    public User findByStaffNo(int staffNo){
        return userRepository.findByStaffNo(staffNo);
    }

    @PostMapping("/register")
    public String save(@RequestBody User user){
        user.setPassword(encryptBASE64(user.getPassword().getBytes()));//加密后存入数据库
        User user2 = userRepository.save(user);
        if(user2 != null)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }

    @PutMapping("/update")
    public String update(@RequestBody User user){
        // user.setPassword();//加密后存入数据库
        int res = userRepository.updateByStaffNo(user.getUsername(), encryptBASE64(user.getPassword().getBytes()), user.getAccess(), user.getStaffNo());
        if(res==1)
        {
            return "{\"Message\":\"success\"}";
        }
        else return "{\"Message\":\"error\"}";
    }

    @DeleteMapping("/deleteByStaffNo")
    public String delete(int StaffNo){
        userRepository.deleteByStaffNo(StaffNo);
        return "{\"Message\":\"success\"}";
    }

    // @PostMapping("/login")
    // public LoginInfo checkAccount(String username,String password) throws Exception {//加密后与数据库对比
    //     password = encryptBASE64(password.getBytes());//加密密码
    //     LoginInfo info = new LoginInfo();
    //     User user = userRepository.findByUsername(username);
    //     Staff staff = new Staff();
    //     if(user != null&&user.getPassword().equals(password))
    //     {
    //         staff = staffRepository.findById(user.getStaff_no()).get();//获取对应员工信息
    //         info.setMessage("success");
    //         info.setUserName(username);
    //         info.setStaff_no(staff.getStaff_no());
    //         info.setStaffName(staff.getStaff_name());
    //         info.setStaffPosition(staff.getStaff_position());
    //         info.setAccess(staff.getStaff_access());
    //     }
    //     else{
    //         info.setMessage("error");
    //     }
    //     return info;
    // }

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
