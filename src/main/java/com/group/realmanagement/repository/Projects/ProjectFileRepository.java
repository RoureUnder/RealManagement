package com.group.realmanagement.repository.Projects;

import java.util.List;

import javax.transaction.Transactional;

import com.group.realmanagement.entity.Projects.ProjectFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProjectFileRepository extends JpaRepository<ProjectFile,Integer>{
    // @Transactional
    // @Modifying
    // @Query(nativeQuery = true,value="insert into staff SET name = ? ,department = ? ,post = ? ,contact = ? ,access = ? ,status = ? WHERE staff_no = ?")
    // ProjectFile save(String name,String department,String post,String contact,int access,int status,int staffNo);
    List<ProjectFile> findByProjectNo(int projectNo);
}
