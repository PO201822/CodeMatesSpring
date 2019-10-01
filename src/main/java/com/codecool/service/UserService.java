package com.codecool.service;

import com.codecool.dto.UserLocationDto;
import com.codecool.entity.Users;

public interface UserService {

    Users getCurrentUser();

    void registerUser(Users user);

    UserLocationDto getLocation();

    void updateProfile(Users updatedUser);

}
