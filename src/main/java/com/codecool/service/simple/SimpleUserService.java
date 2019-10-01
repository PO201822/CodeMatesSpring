package com.codecool.service.simple;

import com.codecool.dto.UserLocationDto;
import com.codecool.entity.Users;
import com.codecool.exception.InvalidUserCredentialsException;
import com.codecool.repository.UserRepository;
import com.codecool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements UserService {


    @Autowired
    private UserRepository userRepository;

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = (String) authentication.getPrincipal();
        return userRepository.findByName(name);
    }

    public void registerUser(Users user) {
        if (userRepository.findByName(user.getName()) == null
                && userRepository.findByEmail(user.getEmail()) == null) {
            userRepository.save(user);
        } else {
            throw new InvalidUserCredentialsException("Email and/or username already exists!");
        }
    }

    public UserLocationDto getLocation() {
        if (getCurrentUser().getName().equals("anonymousUser")) {
            return new UserLocationDto("Miskolc");
        } else {
            return new UserLocationDto(getCurrentUser().getLocation());
        }
    }

    public void updateProfile(Users updatedUser) {
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
