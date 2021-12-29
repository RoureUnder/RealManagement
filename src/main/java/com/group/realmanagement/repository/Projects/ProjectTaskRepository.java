package com.group.realmanagement.repository.Projects;

import java.util.List;

import com.group.realmanagement.entity.Projects.ProjectTask;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask,Integer>{
    List<ProjectTask> findByProjectNo(int projectNo);
    List<ProjectTask> findBySenderNo(int senderNo);
    List<ProjectTask> findByReceiverNo(int receiverNo);
    List<ProjectTask> findByProjectNoAndSenderNo(int projectNo,int senderNo);
}
