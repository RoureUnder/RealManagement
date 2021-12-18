package com.group.realmanagement.entity.Projects;

import java.sql.Date;



import lombok.Data;
@Data
public class ProjectInfoReturn {
    private int no;
    private int projectNo;
    private String projectName;
    private String guestName;
    private String principalName;
    private String modelName;
    private String renderName;
    private String lateName;
    private Date startTime;
    private Date endTime;
    private int status;
}
