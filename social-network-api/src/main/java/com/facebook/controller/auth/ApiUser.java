package com.facebook.controller.auth;

import com.facebook.dto.app_user.AppUserRequest;
import com.facebook.exception.ValidationErrorResponse;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ApiUser {
    private final AppUserService service;
    private final AppUserFacade facade;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/auth/signup", consumes = "application/json")
    public ResponseEntity<?> createAppUser(@Valid @RequestBody AppUserRequest appUserRequest) {
        AppUser user = facade.convertToAppUser(appUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new String[]{"USER"});

        return Optional.of(user)
                .flatMap(service::save)
                .map(facade::convertToAppUserResponse)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body(new ValidationErrorResponse()));
    }
}




