package com.codecool.controller;

import com.codecool.entity.Users;
import com.codecool.exception.InvalidUserCredentialsException;
import com.codecool.model.GoogleUser;
import com.codecool.model.UserCredentials;
import com.codecool.repository.UserRepository;
import com.codecool.security.JwtTokenServices;
import com.codecool.service.simple.SimpleUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenServices jwtTokenServices;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpleUserService simpleUserService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenServices jwtTokenServices) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenServices = jwtTokenServices;
    }


    @PostMapping("/signin")
    public ResponseEntity login(@RequestBody UserCredentials data) {
        try {
            String name = data.getName();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, data.getPassword()));
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = jwtTokenServices.createToken(name, roles);

            Map<Object, Object> model = new HashMap<>();
            model.put("name", name);
            model.put("roles", roles);
            model.put("token", token);
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new InvalidUserCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/googlesignin")
    public ResponseEntity login(@RequestBody GoogleUser googleUser) {
        try {

            String email = googleUser.getEmail();
            String username = email.split("@")[0];
            String id = googleUser.getId();
            List<Users> users = new ArrayList<>();
            users.add(userRepository.findByName(username));

            if (userRepository.findByName(username) == null){
                simpleUserService.registerUser(new Users(username, email, String.valueOf(id.hashCode()), "Miskolc", "Address"));
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, String.valueOf(id.hashCode())));
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = jwtTokenServices.createToken(username, roles);

            Map<Object, Object> model = new HashMap<>();
            model.put("name", username);
            model.put("roles", roles);
            model.put("token", token);
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new InvalidUserCredentialsException("Invalid username/password supplied");
        }
    }
}


