package com.facebook.service;

import com.facebook.config.cache.CacheStore;
import com.facebook.dto.appuser.UserNewPasswordRequest;
import com.facebook.exception.InvalidTokenException;
import com.facebook.exception.UserNotFoundException;
import com.facebook.model.AppUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class ResetPasswordService {
    private final CacheStore<String> resetPasswordTokenCache;
    private final EmailService emailService;
    private final AppUserService appUserService;


    @Autowired
    public ResetPasswordService(CacheStore<String> resetPasswordTokenCache, EmailService emailService, AppUserService appUserService) {
        this.resetPasswordTokenCache = resetPasswordTokenCache;
        this.emailService = emailService;
        this.appUserService = appUserService;
    }

    String generateToken(){
        return UUID.randomUUID().toString();
    }
    String createAndAddResetPasswordToken(String email){
        String newToken = generateToken();
        resetPasswordTokenCache.add(email, newToken);
        return newToken;
    }

    public boolean isResetTokenValid(String token, String email){
        String tokenFromCache = resetPasswordTokenCache.get(email);
        if(tokenFromCache == null) throw new InvalidTokenException();
        return tokenFromCache.equals(token);
    }
    public void sendResetPasswordLink(String email, String url) {
        Optional<AppUser> user = appUserService.findByEmail(email);
        if(user.isEmpty()) throw new UserNotFoundException();
        try{
            String resetPasswordToken = createAndAddResetPasswordToken(email);
            log.info("token " + resetPasswordToken);
            emailService.sendResetPasswordEmail(email, resetPasswordToken, url);
        } catch (Exception e){
            log.error("Error sending reset password email", e);
        }
    }
    public void resetUserPassword(String token, UserNewPasswordRequest user){
        if(!isResetTokenValid(token, user.getEmail())) throw new InvalidTokenException();

        appUserService.updatePassword(user.getEmail(), user.getNewPassword());
        resetPasswordTokenCache.removeToken(user.getEmail());
    }
}

