package com.facebook.controller.auth;

import com.facebook.config.security.JwtTokenService;
import com.facebook.dto.appuser.AppUserRequest;
import com.facebook.dto.appuser.LoginRequest;
import com.facebook.dto.appuser.LoginResponse;
import com.facebook.exception.ValidationErrorResponse;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import jakarta.validation.Valid;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@Validated
@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ApiUser {

    private final AppUserService service;

    private final AppUserFacade facade;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenService tokenService;

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

    @PostMapping("/auth/token")
    public ResponseEntity<LoginResponse> handleLogin(@RequestBody LoginRequest rq) {
        return service.findByUsername(rq.username())
                .filter(u -> passwordEncoder.matches(rq.password(), u.getPassword()))
                .map(u -> LoginResponse.Ok(u.getId(),
                        tokenService.generateToken(Math.toIntExact(u.getId())), u.getRoles()))
                .map(loginResponseOk -> {
                    log.info("AuthenticationController: Token sent!");
                    return ResponseEntity.ok(loginResponseOk);
                })
                .orElseGet(() -> {
                    log.error("AuthenticationController: User not "
                            + "found or wrong user/password combination");
                    return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .body(LoginResponse.Error("wrong user/password combination"));
                });
    }
}




