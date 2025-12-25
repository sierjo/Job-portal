package com.spring_boot_project.jobportal.entity;

public interface IRecruiterJobs {
    Long getTotalCandidates();

    int getJob_post_id();

    String getJob_title();

    int getLocationID();

    String getCity();

    String getState();

    String getCountry();

    int getCompanyId();

    String getName();

}
