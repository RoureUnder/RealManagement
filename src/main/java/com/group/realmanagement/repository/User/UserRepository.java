package com.group.realmanagement.repository.User;

import javax.transaction.Transactional;

import com.group.realmanagement.entity.User.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Integer>{
    User findByUsername(String username);
    User findByStaffNo(int staffNo);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="UPDATE user SET username = ?,password = ?,access = ? WHERE staff_no = ?")
    int updateByStaffNo(String username,String password,int access,int staffNo);


    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="DELETE FROM user WHERE staff_no = ?")
    int deleteByStaffNo(int staffNo);
}
