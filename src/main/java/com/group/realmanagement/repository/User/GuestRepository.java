package com.group.realmanagement.repository.User;

import java.util.List;

import javax.transaction.Transactional;

import com.group.realmanagement.entity.User.Guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GuestRepository extends JpaRepository<Guest,Integer>{
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="UPDATE guest SET guest_name = ?, primary_unit = ?, secondary_unit = ?, tertiary_unit = ?, telephone = ?, mobile_phone = ?, qq = ?, email = ? where guest_no = ?")
    int updateByGuestNo(String guestName, String primaryUnit, String secondaryUnit, String tertiaryUnit, String telephone, String mobilePhone, String qq, String email,int guestNo);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value="DELETE FROM guest WHERE guest_no = ?")
    int deleteByGuestNo(int guestNo);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "SELECT *FROM guest WHERE guest_name like %?%")
    List<Guest> findByGuestName(String guestName);

    Guest findByGuestNo(int guestNo);


    
}
