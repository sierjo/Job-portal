package com.spring_boot_project.jobportal.repository;

import com.spring_boot_project.jobportal.entity.JobPostActivity;
import com.spring_boot_project.jobportal.entity.JobSeekerProfile;
import com.spring_boot_project.jobportal.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave,Integer> {
    List<JobSeekerSave>findByUserId(JobSeekerProfile userAccountId);
    List<JobSeekerSave>findByJob(JobPostActivity userAccountId);
}
