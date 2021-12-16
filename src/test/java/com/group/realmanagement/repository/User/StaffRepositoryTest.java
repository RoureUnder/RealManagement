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
        System.out.println(staffRepository.findByStaffName("staff1"));
    }



    @Test
    void testFileTest(){
        String path="D:/项目/项目名-客户名-客户单位-日期/资料/temp.jpg";
        String path2="D:/项目/项目名-客户名-客户单位-日期/渲染文件/原始模型/temp.jpg";
        String a="D:",b="项目",c="项目名-客户名-客户单位-日期";

        System.out.println(a+"/"+b+"/"+c);
    }
}
