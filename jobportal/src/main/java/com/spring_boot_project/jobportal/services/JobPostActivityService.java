package com.spring_boot_project.jobportal.services;

import com.spring_boot_project.jobportal.entity.*;
import com.spring_boot_project.jobportal.repository.JobPostActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class JobPostActivityService {

    private final JobPostActivityRepository jobPostActivityRepository;

    @Autowired
    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public JobPostActivity addNew(JobPostActivity jobPostActivity) {
        return jobPostActivityRepository.save(jobPostActivity);
    }

    public List<RecruiterJobsDTO> getRecruiterJobs(int recruiter) {
        /*jobPostActivityRepository.getRecruiterJobs(recruiter);
         *  + Cnrl + alt + v*/
        List<IRecruiterJobs> recruiterJobsDtos = jobPostActivityRepository.getRecruiterJobs(recruiter);
        List<RecruiterJobsDTO> recruiterJobsDTOList = new ArrayList<>();
        for (IRecruiterJobs rec : recruiterJobsDtos) { // Convert info from database to DTOs
            JobLocation loc = new JobLocation(rec.getLocationID(), rec.getCity(), rec.getState(), rec.getCountry());

            // So as I'm going through this loop, will construct a DTO based on information that we retrieved from the database.
            /* new JobCompany(rec.getCompanyId(),rec.getName(),"");
             *  + Cnrl + alt + v*/
            JobCompany comp = new JobCompany(rec.getCompanyId(), rec.getName(), "");
            recruiterJobsDTOList.add(new RecruiterJobsDTO(rec.getTotalCandidates(), rec.getJob_post_id(),
                    rec.getJob_title(), loc, comp));
        }
        return recruiterJobsDTOList;
    }

    public JobPostActivity getOne(int id) {
        return jobPostActivityRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public List<JobPostActivity> search(String job, String location, List<String> type, List<String> remote,
                                        LocalDate searchDate) {
        return Objects.isNull(searchDate) ? jobPostActivityRepository.searchWithoutDate(job, location, remote, type) :
                jobPostActivityRepository.search(job, location, remote, type, searchDate);
    }
}