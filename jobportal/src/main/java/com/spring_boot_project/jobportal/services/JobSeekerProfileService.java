package com.spring_boot_project.jobportal.services;

import com.spring_boot_project.jobportal.entity.JobSeekerProfile;
import com.spring_boot_project.jobportal.entity.RecruiterProfile;
import com.spring_boot_project.jobportal.entity.Users;
import com.spring_boot_project.jobportal.repository.JobSeekerProfileRepository;
import com.spring_boot_project.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public JobSeekerProfileService(JobSeekerProfileRepository jobSeekerProfileRepository, UsersRepository usersRepository) {
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<JobSeekerProfile> getOne(Integer id) {
        return jobSeekerProfileRepository.findById(id);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile) {
        return jobSeekerProfileRepository.save(jobSeekerProfile);
    }

    public JobSeekerProfile getCurrentSeekerProfile() {
        /* SecurityContextHolder.getContext().getAuthentication();
         *  + Cnrl + alt + v*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            /* usersRepository.findByEmail(currentUsername).orElseThrow(()->new UsernameNotFoundException("User not found"));
             *  + Cnrl + alt + v*/
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            /* getOne(users.getUserId());
             *  + Cnrl + alt + v*/
            Optional<JobSeekerProfile> seekerProfile = getOne(users.getUserId());
            return seekerProfile.orElse(null);
        } else return null;
    }
}
