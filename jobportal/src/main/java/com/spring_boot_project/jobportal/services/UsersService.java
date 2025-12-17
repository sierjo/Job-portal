package com.spring_boot_project.jobportal.services;

import com.spring_boot_project.jobportal.entity.JobSeekerProfile;
import com.spring_boot_project.jobportal.entity.RecruiterProfile;
import com.spring_boot_project.jobportal.entity.Users;
import com.spring_boot_project.jobportal.repository.JobSeekerProfileRepository;
import com.spring_boot_project.jobportal.repository.RecruiterProfileRepository;
import com.spring_boot_project.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    // add a PasswordEncoder
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, JobSeekerProfileRepository
            jobSeekerProfileRepository, RecruiterProfileRepository
                                recruiterProfileRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.jobSeekerProfileRepository = jobSeekerProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder; // also add in constructor
    }

    public Users addNew(Users users) {
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));

        // In registration process the user password will be BCrypted ↓
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        // In registration process the user password will be BCrypted ↑
        Users saveUser = usersRepository.save(users);

        int userTypeId = users.getUserTypeId().getUserTypeId();
        if (userTypeId == 1) {
            recruiterProfileRepository.save(new RecruiterProfile(saveUser));
        } else {
            jobSeekerProfileRepository.save(new JobSeekerProfile(saveUser));
        }
        return saveUser;
    }

    public Optional<Users> getUsersByEmail(String email) {
        return usersRepository.findByEmail(email);
    }
}
