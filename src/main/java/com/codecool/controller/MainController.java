package com.codecool.controller;

import com.codecool.model.JWTToken;
import com.codecool.repository.UserRepository;
import com.codecool.security.JwtTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MainController {

    @Autowired
    private UserRepository userRepository;

    private JwtTokenServices jwtTokenServices;

    MainController(JwtTokenServices jwtTokenServices) {
        this.jwtTokenServices = jwtTokenServices;
    }

    @PostMapping(path="/profile")
    public Optional getProfile(@RequestBody JWTToken data) {
        String token = data.getToken();
        token = token.replaceAll("\"", "");
        if (jwtTokenServices.validateToken(token)) {
            Authentication auth = jwtTokenServices.parseUserFromTokenInfo(token);
            String name = auth.getName();
            return userRepository.findByName(name);
        }
        return null;
    }
}

