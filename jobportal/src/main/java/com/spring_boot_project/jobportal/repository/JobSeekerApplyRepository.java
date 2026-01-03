package com.spring_boot_project.jobportal.repository;

import com.spring_boot_project.jobportal.entity.JobPostActivity;
import com.spring_boot_project.jobportal.entity.JobSeekerApply;
import com.spring_boot_project.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {
List<JobSeekerApply>findByUserId(JobSeekerProfile userId);
List<JobSeekerApply>findByJob(JobPostActivity job);
}
