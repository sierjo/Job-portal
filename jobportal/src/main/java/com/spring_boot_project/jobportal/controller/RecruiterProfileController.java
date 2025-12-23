package com.spring_boot_project.jobportal.controller;

import com.spring_boot_project.jobportal.entity.RecruiterProfile;
import com.spring_boot_project.jobportal.entity.Users;
import com.spring_boot_project.jobportal.repository.UsersRepository;
import com.spring_boot_project.jobportal.services.RecruiterProfileService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    private final UsersRepository usersRepository; // Set up a reference here for UsersRepository
    private final RecruiterProfileService recruiterProfileService; // Set up a reference here for RecruiterProfileService

    public RecruiterProfileController(UsersRepository usersRepository, RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileService = recruiterProfileService;
    }


    @GetMapping("/")
    private String recruiterProfile(Model model) {
        /*SecurityContextHolder.getContext();
         *  + Cnrl + alt + v*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Login Recruiter section ↓
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            /*authentication.getName();
             *  + Cnrl + alt + v
             * ↓*/
            String currentUsername = authentication.getName();
            /*usersRepository.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("User not found"));
             * Cnrl + alt + v*/
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not found"));


            /*recruiterProfileService.getOne(users.getUserId());
             *  + Cnrl + alt + v
             * ↓*/
            Optional<RecruiterProfile> recruiterProfile = recruiterProfileService.getOne(users.getUserId());

            if (!recruiterProfile.isEmpty()) {
                model.addAttribute("profile", recruiterProfile.get());
            }
        }
        return "recruiter_profile";                                                             // Login Recruiter section ↑
    }
}
