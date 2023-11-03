package com.facebook.service;

import com.facebook.config.cache.CacheStore;
import com.facebook.dto.appuser.UserNewPasswordRequest;
import com.facebook.exception.EmailSendingException;
import com.facebook.exception.InvalidTokenException;
import com.facebook.exception.UserNotFoundException;
import com.facebook.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ResetPasswordService.class)
class ResetPasswordServiceTest {

    @MockBean
    @Qualifier("resetPasswordTokenCache")
    private CacheStore<String> resetPasswordTokenCache;

    @MockBean
    private EmailHandlerService emailHandler;

    @MockBean
    private AppUserService appUserService;

    @Autowired
    private ResetPasswordService resetPasswordService;

    private final String EMAIL = "test@example.com";
    private final String TOKEN = "validToken";
    private final String INVALID_TOKEN = "invalidToken";

    @Test
    void testGenerateToken() {
        String token = resetPasswordService.generateToken();
        assertNotNull(token);
    }

    @Test
    void testCreateAndAddResetPasswordToken() {
        String token = resetPasswordService.createAndAddResetPasswordToken(EMAIL);
        verify(resetPasswordTokenCache, times(1)).add(EMAIL, token);
        when(resetPasswordTokenCache.get(EMAIL)).thenReturn(token);
    }

    @Test
    void testIsResetTokenValidWithValidToken() {
        when(resetPasswordTokenCache.get(EMAIL)).thenReturn(TOKEN);
        boolean isValid = resetPasswordService.isResetTokenValid(TOKEN, EMAIL);
        assertTrue(isValid);
    }

    @Test
    void testIsResetTokenValidWithInvalidToken() {
        when(resetPasswordTokenCache.get(EMAIL)).thenReturn(TOKEN);
        boolean resetTokenValid = resetPasswordService.isResetTokenValid(INVALID_TOKEN, EMAIL);
        assertFalse(resetTokenValid);
    }

    @Test
    void testSendResetPasswordLinkWithExistingUser() throws EmailSendingException {
        AppUser user = new AppUser();
        when(appUserService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        resetPasswordService.sendResetPasswordLink(EMAIL);

        verify(emailHandler, times(1)).sendEmail(anyString(), anyString(), anyString());

    }

    @Test
    void testSendResetPasswordLinkWithNonExistingUser() {
        when(appUserService.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> resetPasswordService.sendResetPasswordLink(EMAIL));
    }

    @Test
    void testResetUserPasswordWithValidToken() {
        UserNewPasswordRequest user = new UserNewPasswordRequest();
        user.setEmail(EMAIL);
        user.setNewPassword("newPassword");
        when(resetPasswordTokenCache.get(EMAIL)).thenReturn(TOKEN);

        resetPasswordService.resetUserPassword(TOKEN, user);

        verify(appUserService, times(1)).updatePassword(user.getEmail(), user.getNewPassword());
        verify(resetPasswordTokenCache, times(1)).remove(EMAIL);
    }

    @Test
    void testResetUserPasswordWithInvalidToken() {

        UserNewPasswordRequest user = new UserNewPasswordRequest();
        user.setEmail(EMAIL);
        user.setNewPassword("newPassword");
        when(resetPasswordTokenCache.get(EMAIL)).thenReturn(TOKEN);

        assertThrows(InvalidTokenException.class, () -> resetPasswordService.resetUserPassword(INVALID_TOKEN, user));
    }

    @Test
    void testSendResetPasswordEmail() throws Exception {
        String URL = "http://localhost:3000/change_password/" + TOKEN + "?em=" + EMAIL;
        resetPasswordService.sendResetPasswordEmail(EMAIL, TOKEN);
        String content ="<p>Click the link below to reset your password:<br>"
                +"<a href="+URL+">Reset password</a>"
                +"<br>This link is valid for 15 minutes.<br>"
                +"If you didn't request password change just ignore this letter.</div>";
        verify(emailHandler).sendEmail(eq(EMAIL), eq("Reset password"), contains(content));

    }

}
