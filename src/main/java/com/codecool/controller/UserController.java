package com.codecool.controller;

import com.codecool.dto.RoleDto;
import com.codecool.dto.UserLocationDto;
import com.codecool.entity.Users;
import com.codecool.repository.UserRepository;
import com.codecool.service.simple.SimpleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private SimpleUserService simpleUserService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/getProfile")
    public Users getProfile() {
        return simpleUserService.getCurrentUser();
    }

    @GetMapping(path = "/public/getLocation")
    public UserLocationDto getLocation() {
        return simpleUserService.getLocation();
    }

    @GetMapping(path = "/getRole")
    public RoleDto getRole() {
        return new RoleDto(simpleUserService.getCurrentUser().getRoles()[0]);
    }

    @PutMapping(path = "/updateProfile")
    public @ResponseBody
    void updateProfile(@RequestBody Users updatedUser) {
        simpleUserService.updateProfile(updatedUser);
    }

    @PostMapping("/register")
    public void register(@RequestBody Users user) {
        simpleUserService.registerUser(user);
    }
}

