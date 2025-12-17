package com.spring_boot_project.jobportal.services;

import com.spring_boot_project.jobportal.entity.Users;
import com.spring_boot_project.jobportal.repository.UsersRepository;
import com.spring_boot_project.jobportal.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Autowired
    public CustomUserDetailService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = usersRepository.findByEmail(username).orElseThrow(()->
                new UsernameNotFoundException("Couldn't found user"));
        return new CustomUserDetails(user);
    }
}
