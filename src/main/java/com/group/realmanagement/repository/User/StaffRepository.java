package com.group.realmanagement.repository.User;

import java.util.List;

import javax.transaction.Transactional;

import com.group.realmanagement.entity.User.Staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface StaffRepository extends JpaRepository<Staff,Integer>{

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="UPDATE staff SET name = ? ,department = ? ,post = ? ,contact = ? ,access = ? ,status = ? WHERE staff_no = ?")
    int updateByStaffNo(String name,String department,String post,String contact,int access,int status,int staffNo);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="DELETE FROM staff WHERE staff_no = ?")
    int deleteByStaffNo(int staffNo);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "SELECT *FROM staff WHERE name like %?%")
    List<Staff> findByStaffName(String name);
}
