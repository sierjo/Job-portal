package com.spring_boot_project.jobportal.services;

import com.spring_boot_project.jobportal.entity.RecruiterProfile;
import com.spring_boot_project.jobportal.entity.Users;
import com.spring_boot_project.jobportal.repository.RecruiterProfileRepository;
import com.spring_boot_project.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {
    private final RecruiterProfileRepository recruiterProfileRepository; // Set up a reference here for RecruiterProfileRepository
    private final UsersRepository usersRepository;

    @Autowired
    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UsersRepository usersRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<RecruiterProfile> getOne(Integer id) {
        return recruiterProfileRepository.findById(id);
    }

    // Create method to -> Save Recruiter profile in Database
    public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
        return recruiterProfileRepository.save(recruiterProfile);
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
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
            Optional<RecruiterProfile> recruiterProfile = getOne(users.getUserId());
            return recruiterProfile.orElse(null);
        } else return null;
    }
}
