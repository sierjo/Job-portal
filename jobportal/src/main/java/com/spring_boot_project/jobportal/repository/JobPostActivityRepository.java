package com.spring_boot_project.jobportal.repository;

import com.spring_boot_project.jobportal.entity.JobPostActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostActivityRepository extends JpaRepository<JobPostActivity,Integer> {
}
