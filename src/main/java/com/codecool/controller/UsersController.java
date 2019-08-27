package com.codecool.controller;

import com.codecool.model.Users;
import com.codecool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RestController
public class UsersController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @PutMapping(path = "/profile")
    public @ResponseBody
    void updateProfile(@RequestBody Users user) {
        int a = 1;

        Users userBU = userRepository.findByEmail(user.getEmail());

        userBU.setName(user.getName());
        userBU.setAddress(user.getAddress());
        userBU.setPassword(user.getPassword());
        userBU.setLocation(user.getLocation());


        userRepository.save(userBU);



    }
}
