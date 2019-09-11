package com.codecool.controller;

import com.codecool.dto.RoleDto;
import com.codecool.entity.Users;
import com.codecool.repository.UserRepository;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PutMapping(path = "/profile")
    public @ResponseBody
    void updateProfile(@RequestBody Users user) {
        Users userBU = userRepository.findByName(user.getName());

        if (!user.getEmail().equals(userBU.getEmail()) &&
                userRepository.findByEmail(user.getEmail()) != null) {
            throw new BadCredentialsException("New email address already exists!");

        }

        userBU.setAddress(user.getAddress());
        userBU.setPassword(user.getPassword());
        userBU.setLocation(user.getLocation());
        userBU.setEmail(user.getEmail());
        userBU.setRoles(user.getRoles());

        userRepository.save(userBU);

    }

    @PostMapping(path = "/getrole")
    public @ResponseBody
    RoleDto getUserRole() {
        Users user = userRepository.findByName(userService.currentUser());
        return new RoleDto(user.getRoles()[0]);
    }
}
