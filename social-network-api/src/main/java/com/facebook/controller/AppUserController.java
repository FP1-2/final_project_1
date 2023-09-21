package com.facebook.controller;

import com.facebook.dto.appuser.UserNewPasswordRequest;
import com.facebook.exception.InvalidTokenException;
import com.facebook.service.AppUserService;
import com.facebook.service.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Validated
public class AppUserController {

    private final AppUserService userService;

    private final ResetPasswordService resetPasswordService;


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody String email){
        resetPasswordService.sendResetPasswordLink(email);
        log.info("ResetPasswordToken created. Reset password link sent.");
        return ResponseEntity.ok("Reset password link sent successfully!");
    }

    @PutMapping(value ="/update-password/{token}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePassword(@PathVariable String token,
                                                 @Valid @RequestBody UserNewPasswordRequest user) {
        resetPasswordService.resetUserPassword(token, user);
        return ResponseEntity.ok("Password reset successful");
    }

}
