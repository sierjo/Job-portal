package com.spring_boot_project.jobportal.repository;

import com.spring_boot_project.jobportal.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    // Duplicate email
    Optional<Users> findByEmail(String email);
}
