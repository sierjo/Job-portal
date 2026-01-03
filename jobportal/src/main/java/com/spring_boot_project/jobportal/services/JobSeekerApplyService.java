package com.spring_boot_project.jobportal.services;

import com.spring_boot_project.jobportal.entity.JobPostActivity;
import com.spring_boot_project.jobportal.entity.JobSeekerApply;
import com.spring_boot_project.jobportal.entity.JobSeekerProfile;
import com.spring_boot_project.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSeekerApplyService {

    private final JobSeekerApplyRepository jobSeekerApplyRepository;

    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile usseAccountId) {
        return jobSeekerApplyRepository.findByUserId(usseAccountId);
    }

    public List<JobSeekerApply> getJobCandidates(JobPostActivity job) {
        return jobSeekerApplyRepository.findByJob(job);
    }

    public void addNew(JobSeekerApply jobSeekerApply) {
        jobSeekerApplyRepository.save(jobSeekerApply);
    }
}
