package com.codecool.security;

import com.codecool.model.Users;
import com.codecool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        BCryptPasswordEncoder encoder = passwordEncoder();
        System.out.println(userRepo.findByName(name));
        Users user = userRepo.findByName(name)
            .orElseThrow(() -> new UsernameNotFoundException("Username: " + name + " not found"));

        return new org.springframework.security.core.userdetails.User(user.getName(),encoder.encode(user.getPassword()), Arrays.asList(user.getRoles()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
