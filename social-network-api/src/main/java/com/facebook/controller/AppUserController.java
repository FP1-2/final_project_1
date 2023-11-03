package com.facebook.controller;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.appuser.AppUserEditRequest;
import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.appuser.UserNewPasswordRequest;
import com.facebook.exception.UserNotFoundException;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import com.facebook.service.CurrentUserService;
import com.facebook.service.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Validated
public class AppUserController {

    private final ResetPasswordService resetPasswordService;

    private final AppUserService appUserService;

    private final CurrentUserService currentUserService;

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
    public ResponseEntity<AppUserResponse> getUserAppById(@PathVariable long id) {
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

    @PutMapping("/update-password/{token}")
    public ResponseEntity<String> updatePassword(@PathVariable String token,
                                                 @Valid @RequestBody UserNewPasswordRequest user) {
        resetPasswordService.resetUserPassword(token, user);
        return ResponseEntity.ok("Password reset successful");
    }

    @PutMapping("/edit")
    public ResponseEntity<AppUserResponse> editUserInfo(@Valid @RequestBody AppUserEditRequest userEditReq) {
        Long id = currentUserService.getCurrentUserId();
        return appUserService.editUser(id, userEditReq)
                .map(appUserFacade::convertToAppUserResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AppUserChatResponse>> searchUserByKeyword(@RequestParam String input,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "20") int size){
        Pageable pageable = PageRequest.of(page, size);
        List<AppUserChatResponse> userByKeyword = appUserService.findUserByKeyword(input, pageable);
        return ResponseEntity.ok(userByKeyword);

    }
}
