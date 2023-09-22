package com.facebook.controller;

import com.facebook.dto.appuser.AppUserRequest;
import com.facebook.dto.appuser.ConfirmMessageResponse;
import com.facebook.dto.appuser.LoginRequest;
import com.facebook.dto.appuser.LoginResponse;
import com.facebook.exception.InvalidTokenException;
import com.facebook.exception.UserNotFoundException;
import com.facebook.service.RegistrationAndAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@Validated
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class RegistrationAndAuthController {

    private final RegistrationAndAuthService registrationAndAuthService;

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<SignupResponse> createAppUser(@Valid
                                                        @RequestBody
                                                        AppUserRequest appUserRequest) {
        return ResponseEntity
                .ok(registrationAndAuthService.createAppUser(appUserRequest));

    }

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> handleLogin(@RequestBody
                                                     LoginRequest rq) {
        try {
            log.info("AuthenticationController: Token sent!");
            return ResponseEntity
                    .ok(registrationAndAuthService.handleLogin(rq));
        } catch (IllegalArgumentException e) {
            log.error("AuthenticationController: User not "
                    + "found or wrong user/password combination");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(LoginResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<ConfirmMessageResponse> confirmRegistration(@RequestParam String token, @RequestParam String em) {
        try {
            registrationAndAuthService.confirmRegistration(token, em);
            return ResponseEntity.ok(new ConfirmMessageResponse("Registration has been successfully confirmed!"));
        } catch (InvalidTokenException | UserNotFoundException e) {
            log.error("Error confirming registration", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ConfirmMessageResponse("Invalid token or user not found"));
        }
    }

}




