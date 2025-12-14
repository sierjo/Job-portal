package com.spring_boot_project.jobportal.repository;

import com.spring_boot_project.jobportal.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersTypeRepository extends JpaRepository<UsersType,Integer> {
}
