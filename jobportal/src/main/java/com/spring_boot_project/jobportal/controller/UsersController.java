package com.spring_boot_project.jobportal.controller;

import com.spring_boot_project.jobportal.entity.Users;
import com.spring_boot_project.jobportal.entity.UsersType;
import com.spring_boot_project.jobportal.services.UsersService;
import com.spring_boot_project.jobportal.services.UsersTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {
    private final UsersTypeService usersTypeService;
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersTypeService usersTypeService, UsersService usersService) {
        this.usersTypeService = usersTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        List<UsersType> usersTypes = usersTypeService.getAll();
        model.addAttribute("getAllTypes", usersTypes);
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid Users users, Model model) {
//        System.out.println("User:: " + users);

// Processing an error message related to email duplication ↓
        Optional<Users> optionalUsers = usersService.getUsersByEmail(users.getEmail());

        if (optionalUsers.isPresent()) {
            model.addAttribute("error", "Email already registration, try to login ore registration with other email.");
            List<UsersType> usersTypes = usersTypeService.getAll();
            model.addAttribute("getAllTypes", usersTypes);
            model.addAttribute("user", new Users());
            return "register";
// END Processing an error message related to email duplication ↑
        }
        usersService.addNew(users);
        return "dashboard";
    }
}
