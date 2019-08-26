package com.codecool.controller;

import com.codecool.model.Users;
import com.codecool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public Users register(@RequestBody Users data) {
        String name = data.getName();
        String password = data.getPassword();
        String email = data.getEmail();
        String location = data.getLocation();
        String address = data.getAddress();

        if(userRepository.findByEmail(email).equals(Optional.empty()) ){
            Users newUser = new Users(name, email, password, location, address);
            return userRepository.save(newUser);
        }
        else {
            throw new BadCredentialsException("Email already exists!");
        }
    }
}
