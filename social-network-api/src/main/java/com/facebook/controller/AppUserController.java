package com.facebook.controller;

import com.facebook.dto.appuser.AppUserRequest;
import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.appuser.UserNewPasswordRequest;
import com.facebook.exception.UserNotFoundException;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import com.facebook.service.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    private final ResetPasswordService resetPasswordService;

    private final AppUserService appUserService;

    private final AppUserFacade appUserFacade;

    @GetMapping("/all")
    public ResponseEntity<Page<AppUserResponse>>
    getAllAppUsers(@PageableDefault(size = 1)
                   Pageable pageable) {
        Page<AppUser> users = appUserService.findAllAppUsers(pageable);
        Page<AppUserResponse> response = users.map(appUserFacade::convertToAppUserResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponse> getUserAppById(@PathVariable Long id) {
        return appUserService.findById(id)
                .map(appUserFacade::convertToAppUserResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(UserNotFoundException::new);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody String email) {
        resetPasswordService.sendResetPasswordLink(email);
        log.info("ResetPasswordToken created. Reset password link sent.");
        return ResponseEntity.ok("Reset password link sent successfully!");
    }

    @PutMapping(value = "/update-password/{token}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePassword(@PathVariable String token,
                                                 @Valid @RequestBody UserNewPasswordRequest user) {
        resetPasswordService.resetUserPassword(token, user);
        return ResponseEntity.ok("Password reset successful");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody AppUserRequest userReq) {
        appUserService.updateUserById(id, userReq);
        return ResponseEntity.ok("Information update successfully");
    }
    
}
