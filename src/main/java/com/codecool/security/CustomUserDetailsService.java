package com.codecool.security;

import com.codecool.model.Users;
import com.codecool.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    //Loads the user from the DB and converts it to Spring Security's internal User object
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Users user = users.findByName(name)
            .orElseThrow(() -> new UsernameNotFoundException("Username: " + name + " not found"));

        return new User(user.getName(), user.getPassword(),  user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
