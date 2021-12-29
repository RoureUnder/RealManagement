package com.group.realmanagement.repository.Projects;

import java.util.List;
import com.group.realmanagement.entity.Projects.ProjectFileReview;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectFileReviewRepository extends JpaRepository<ProjectFileReview, Integer> {
    List<ProjectFileReview> findByProjectNo(int projectNo);

    List<ProjectFileReview> findByPrincipalNo(int principalNo);

    List<ProjectFileReview> findByUploader(int uploader);

    ProjectFileReview findByTaskNo(int taskNo);
}
