package com.codecool.controller;

import com.codecool.entity.Users;
import com.codecool.exception.InvalidUserCredentialsException;
import com.codecool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {

        if (userRepository.findByName(user.getName()) == null
                && userRepository.findByEmail(user.getEmail()) == null) {
            return userRepository.save(user);
        } else {
            throw new InvalidUserCredentialsException("Email and/or username already exists!");
        }
    }
}
