package com.spring_boot_project.jobportal.controller;

import com.spring_boot_project.jobportal.entity.*;
import com.spring_boot_project.jobportal.services.JobPostActivityService;
import com.spring_boot_project.jobportal.services.JobSeekerApplyService;
import com.spring_boot_project.jobportal.services.JobSeekerSaveService;
import com.spring_boot_project.jobportal.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {
    public final UsersService usersService;
    public final JobPostActivityService jobPostActivityService;
    public final JobSeekerApplyService jobSeekerApplyService;
    public final JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }


    //  In which job we will search  (writing param)
    @GetMapping("/dashboard/")
    public String searchJobs(Model model,
                             @RequestParam(value = "job", required = false) String job,
                             @RequestParam(value = "location", required = false) String location,
                             @RequestParam(value = "partTime", required = false) String partTime,
                             @RequestParam(value = "fullTime", required = false) String fullTime,
                             @RequestParam(value = "freeLance", required = false) String freeLance,
                             @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                             @RequestParam(value = "officeOnly", required = false) String officeOnly,
                             @RequestParam(value = "partialRemote", required = false) String partialRemote,
                             @RequestParam(value = "today", required = false) String today,
                             @RequestParam(value = "days7", required = false) String days7,
                             @RequestParam(value = "days30", required = false) String days30
    ) {
        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freeLance", Objects.equals(fullTime, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;

        boolean dataSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if (days30) {
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        } else if (today) {
            searchDate = LocalDate.now();
        } else {
            dataSearchFlag = false;
        }
        if (partTime == null && fullTime == null && freeLance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freeLance = "Freelance";
            remote = false;
        }
        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }
        if (!dataSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPost = jobPostActivityService.getAll();
        } else {
            jobPost = jobPostActivityService.search(job, location, Arrays.asList(partTime, fullTime, freeLance),
                    Arrays.asList(remote, officeOnly, partialRemote), searchDate);
        }

        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
            /*
             * Если залогиненый пользователь является рекрутром то
             * */
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                /* jobPostActivityService.getRecruiterJobs(((RecruiterProfile)currentUserProfile).getUserAccoutId());
                 *  + Cnrl + alt + v*/
                List<RecruiterJobsDTO> recruiterJobs = jobPostActivityService.getRecruiterJobs(((RecruiterProfile)
                        currentUserProfile).getUserAccountId());
                model.addAttribute("jobPost", recruiterJobs);
            } else {
                /* jobSeekerApplyService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);
                 *  + Cnrl + alt + v*/
                List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidatesJobs((JobSeekerProfile)
                        currentUserProfile);
                /* jobSeekerApplyService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);
                 *  + Cnrl + alt + v*/
                List<JobSeekerSave> JobSeekerSaveList = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
                boolean exist;
                boolean saved;
                for (JobPostActivity jobActivity : jobPost) {
                    exist = false;
                    saved = false;
                    for (JobSeekerApply jobSeekerApply : jobSeekerApplyList) {
                        if (Objects.equals(jobActivity.getJobPostId(), jobSeekerApply.getJob().getJobPostId())) {
                            jobActivity.setIsActive(true);
                            exist = true;
                            break;
                        }
                    }
                    for (JobSeekerSave jobSeekerSave:JobSeekerSaveList){
                        if (Objects.equals(jobActivity.getJobPostId(), jobSeekerSave.getJob().getJobPostId())) {
                            jobActivity.setIsActive(true);
                            saved = true;
                            break;
                    }
                }
                    if (!exist){
                    jobActivity.setIsActive(false);}
                    if (!saved){
                    jobActivity.setIsActive(false);}
                    model.addAttribute("jobPost",jobPost);
                }
            }
        }
        model.addAttribute("user", currentUserProfile);
        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model) {
        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", usersService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/dashboard/addNew")
    public String addNew(JobPostActivity jobPostActivity, Model model) {
        Users user = usersService.getCurrentUser();
        if (user != null) {  // if we have a users
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity", jobPostActivity);

        /* jobPostActivityService.addNew(jobPostActivity);
         * Cnrl + alt + v  */
        JobPostActivity saved = jobPostActivityService.addNew(jobPostActivity);
        return "redirect:/dashboard/";
    }
}
