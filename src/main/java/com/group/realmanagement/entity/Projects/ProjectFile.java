package com.group.realmanagement.entity.Projects;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ProjectFile {
    @Id
    private int no;
    private int projectNo;
    private String projectName;
    private String module;
    private String detail;
    private String fileName;
    private String fullPath;
}
