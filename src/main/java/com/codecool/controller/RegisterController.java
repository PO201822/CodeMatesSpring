package com.codecool.controller;

import com.codecool.model.Users;
import com.codecool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
    @PostMapping("/register")
    public Users register(@RequestBody Users data) {
        String name = data.getName();
        String password = data.getPassword();
        String email = data.getEmail();
        String location = data.getLocation();
        String address = data.getAddress();
        String[] roles = new String[]{"USER"};
        Users newUser = new Users(name, email, password, location, address);
        return userRepository.save(newUser);
    }
}
