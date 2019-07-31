package com.codecool.controller;

import com.codecool.model.Users;
import com.codecool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/all")
    public @ResponseBody
    Iterable<Users> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}

