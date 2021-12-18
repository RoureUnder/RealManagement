package com.group.realmanagement.entity.User;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Guest {
    @Id
    private int no;
    private int guestNo;
    private String guestName;
    private String primaryUnit;
    private String secondaryUnit;
    private String tertiaryUnit;
    private String telephone;
    private String mobilePhone;
    private String qq;
    private String email;
}
