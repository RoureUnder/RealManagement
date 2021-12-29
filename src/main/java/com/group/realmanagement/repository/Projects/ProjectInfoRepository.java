package com.group.realmanagement.repository.Projects;

import java.util.Date;

import javax.transaction.Transactional;

import com.group.realmanagement.entity.Projects.ProjectInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Integer> {
    ProjectInfo findByProjectNo(int projectNo);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE project_info SET project_name = ? ,guest_no = ? ,principal_no = ? ,model_no = ? ,render_no = ? ,late_no = ? , start_time = ? , end_time = ? ,status = ? WHERE project_no = ?")
    int updateByProjectNo(String projectName, int guestNo, int principalNo, int modelNo, int renderNo, int lateNo,
            Date startTime, Date endTime, int status, int projectNo);

}
