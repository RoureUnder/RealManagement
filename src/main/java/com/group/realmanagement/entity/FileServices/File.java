package com.group.realmanagement.entity.FileServices;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class File {
    @Id
    private int no;
    private String fileName;
    private String fileUrl;

    public File(int no,String name,String url){
        this.no=no;
        fileName=name;
        fileUrl=url;
    }
}
