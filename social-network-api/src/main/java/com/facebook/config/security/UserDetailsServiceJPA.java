package com.facebook.config.security;

import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class UserDetailsServiceJPA implements UserDetailsService {
    private final AppUserService service;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("-------------- loading user: {}", username);
        return service.findByUsername(username)
                .map(this::mapper)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("user %s not found", username)
                ));
    }

    private UserDetails mapper(AppUser entry) {
        return User
                .withUsername(entry.getName())
                .password(entry.getPassword())
                .roles(Arrays.stream(entry.getRoles())
                        .map(role -> "ROLE_" + role)
                        .toArray(String[]::new))
                .build();
    }
}