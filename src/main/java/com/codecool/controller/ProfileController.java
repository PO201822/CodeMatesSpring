package com.codecool.controller;

import com.codecool.dto.RoleDto;
import com.codecool.dto.UserLocationDto;
import com.codecool.entity.Users;
import com.codecool.exception.InvalidUserCredentialsException;
import com.codecool.repository.UserRepository;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/getProfile")
    public Users getProfile() {
        return userRepository.findByName(userService.currentUser());
    }

    @GetMapping(path = "/getLocation")
    public UserLocationDto getLocation() {
        return new UserLocationDto(userRepository.findByName(userService.currentUser()).getLocation());
    }

    @GetMapping(path = "/getRole")
    public RoleDto getRole() {
        Users user = userRepository.findByName(userService.currentUser());
        return new RoleDto(user.getRoles()[0]);
    }

    @PutMapping(path = "/updateProfile")
    public @ResponseBody
    void updateProfile(@RequestBody Users updatedUser) {
        Users currentUser = userRepository.findByName(updatedUser.getName());

        if (!updatedUser.getEmail().equals(currentUser.getEmail()) &&
                userRepository.findByEmail(updatedUser.getEmail()) != null) {
            throw new InvalidUserCredentialsException("New email address already exists!");
        }

        currentUser.setAddress(updatedUser.getAddress());
        currentUser.setPassword(updatedUser.getPassword());
        currentUser.setLocation(updatedUser.getLocation());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setRoles(updatedUser.getRoles());

        userRepository.save(currentUser);
    }
}

