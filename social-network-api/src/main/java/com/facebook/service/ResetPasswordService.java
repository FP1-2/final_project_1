package com.facebook.service;

import com.facebook.config.cache.CacheStore;
import com.facebook.dto.appuser.UserNewPasswordRequest;
import com.facebook.exception.EmailSendingException;
import com.facebook.exception.InvalidTokenException;
import com.facebook.exception.UserNotFoundException;
import com.facebook.model.AppUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class ResetPasswordService {
    private final CacheStore<String> resetPasswordTokenCache;
    private final EmailHandlerService emailHandlerService;
    private final AppUserService appUserService;

    @Autowired
    public ResetPasswordService(@Qualifier("resetPasswordTokenCache")
                                CacheStore<String> resetPasswordTokenCache,
                                EmailHandlerService emailHandlerService,
                                AppUserService appUserService) {
        this.resetPasswordTokenCache = resetPasswordTokenCache;
        this.emailHandlerService = emailHandlerService;
        this.appUserService = appUserService;
    }

    String generateToken() {
        return UUID.randomUUID().toString();
    }

    String createAndAddResetPasswordToken(String email) {
        String newToken = generateToken();
        resetPasswordTokenCache.add(email, newToken);
        return newToken;
    }

    public boolean isResetTokenValid(String token, String email) {
        String tokenFromCache = resetPasswordTokenCache.get(email);
        if (tokenFromCache == null) throw new InvalidTokenException();
        return tokenFromCache.equals(token);
    }

    public void sendResetPasswordLink(String email, String url) {
        Optional<AppUser> user = appUserService.findByEmail(email);
        if (user.isEmpty()) throw new UserNotFoundException();
        try {
            String resetPasswordToken = createAndAddResetPasswordToken(email);
            log.info("token " + resetPasswordToken);
            sendResetPasswordEmail(email, resetPasswordToken, url);
        } catch (Exception e) {
            log.error("Error sending reset password email", e);
        }
    }

    public void resetUserPassword(String token, UserNewPasswordRequest user) {
        if (!isResetTokenValid(token, user.getEmail())) throw new InvalidTokenException();

        appUserService.updatePassword(user.getEmail(), user.getNewPassword());
        resetPasswordTokenCache.remove(user.getEmail());
    }

    public void sendResetPasswordEmail(String email, String token, String url)
            throws EmailSendingException {
        String resetPasswordLetterSubject = "Reset password";
        String resetPasswordLetterContent = "<p>Click the link below to reset your password:<br>"
                + "<a href=%s>Reset password</a>"
                + "<br>This link is valid for 15 minutes.<br>"
                + "If you didn't request password change just ignore this letter.</div>";
        emailHandlerService.sendEmail(email, resetPasswordLetterSubject,
                String.format(resetPasswordLetterContent, url + "/" + token + "?em=" + email));
    }

}

