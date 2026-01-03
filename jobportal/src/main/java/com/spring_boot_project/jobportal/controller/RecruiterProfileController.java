package com.spring_boot_project.jobportal.controller;

import com.spring_boot_project.jobportal.entity.RecruiterProfile;
import com.spring_boot_project.jobportal.entity.Users;
import com.spring_boot_project.jobportal.repository.UsersRepository;
import com.spring_boot_project.jobportal.services.RecruiterProfileService;
import com.spring_boot_project.jobportal.util.FileUploadUtil;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
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

    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile, Model model) {
        /*SecurityContextHolder.getContext().getAuthentication();
         *  + Cnrl + alt + v
         * ↓*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            /*authentication.getName();
             *  + Cnrl + alt + v
             * ↓*/
            String currentUsername = authentication.getName();
            /*usersRepository.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("User not found"));
             * Cnrl + alt + v*/
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new
                    UsernameNotFoundException("User not found"));
            // Associating recruiter profile with existing user account ↓
            recruiterProfile.setUsersId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
            // End Associating recruiter profile with existing user account ↓
        }
        model.addAttribute("profile", recruiterProfile);
        // Set image Name in Recruiter profile
        String fileName = "";
        if (!multipartFile.getOriginalFilename().equals("")) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileName);
        }
        // End to Set image Name in Recruiter profile

        // Save Recruiter profile in Database
        RecruiterProfile savedUser = recruiterProfileService.addNew(recruiterProfile);
        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();
        try {
            // Read profile image from request multipartfile
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile); // Save image on the server in directory: photos/recruiter
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "redirect:/dashboard/";
    }
}
