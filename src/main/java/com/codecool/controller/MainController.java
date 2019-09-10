package com.codecool.controller;

import com.codecool.entity.Users;
import com.codecool.repository.UserRepository;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/profile")
    public Users getProfile() {
        return userRepository.findByName(userService.currentUser());
    }
}

