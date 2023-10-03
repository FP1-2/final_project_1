package com.facebook.controller;

import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.appuser.UserNewPasswordRequest;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.exception.UserNotFoundException;
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

import java.util.Set;

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

    @PostMapping("/{userId}/subscribe/{targetUserId}")
    public ResponseEntity<?> subscribeToUser(
            @PathVariable Long userId,
            @PathVariable Long targetUserId
    ) {
        // TODO логіка для підписки користувача на іншого користувача
        appUserService.subscribe(userId, targetUserId);
        return ResponseEntity.ok("Subscribed successfully");
    }

    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<?> getUserSubscriptions(@PathVariable Long userId) {
        // TODO Логіка для отримання списку підписок користувача
        Set<AppUser> subscriptions = appUserService.getUserSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSubscription(@PathVariable Long targetUserId) {
    // TODO логіка видалення підписки чи друга
        appUserService.deleteSubscription(targetUserId);
        return ResponseEntity.ok("Unsubscribed successfully");
    }

}
