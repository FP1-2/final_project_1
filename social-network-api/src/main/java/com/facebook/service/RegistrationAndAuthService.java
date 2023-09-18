package com.facebook.service;

import com.facebook.controller.SignupResponse;
import com.facebook.dto.appuser.AppUserRequest;
import com.facebook.dto.appuser.LoginRequest;
import com.facebook.dto.appuser.LoginResponse;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@RequiredArgsConstructor
public class RegistrationAndAuthService {

    private final AppUserService appUserService;
    private final AppUserFacade appUserFacade;
    private final PasswordEncoder passwordEncoder;
    private final EmailHandlerService emailHandler;
    private final JwtTokenService tokenService;

    public SignupResponse createAppUser(AppUserRequest appUserRequest) throws Exception {
        AppUser user = appUserFacade.convertToAppUser(appUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new String[]{"USER"});

        Optional<AppUser> savedUser = appUserService.save(user);

        if (savedUser.isPresent()) {
            SignupResponse response = appUserFacade.convertToAppUserResponse(savedUser.get());

            try {
                sendWelcomeEmail(user);
            } catch (Exception e) {
                sendErrorEmail(user);
            }

            return response;
        } else {
            throw new DataIntegrityViolationException("Error in user registration");
        }
    }


    public LoginResponse handleLogin(LoginRequest rq) {
        return appUserService.findByUsername(rq.username())
                .filter(u -> passwordEncoder.matches(rq.password(), u.getPassword()))
                .map(u -> LoginResponse.Ok(u.getId(),
                        tokenService.generateToken(Math.toIntExact(u.getId())), u.getRoles()))
                .orElseThrow(() -> new IllegalArgumentException("wrong user/password combination"));
    }

    private void sendWelcomeEmail(AppUser user) throws Exception {
        emailHandler.sendEmail(user.getEmail(), "Реєстрацію завершено",
                String.format("Ласкаво просимо, %s! Ваша реєстрація пройшла успішно.",
                        user.getUsername()));
    }

    private void sendErrorEmail(AppUser user) throws Exception {
        emailHandler.sendEmail(user.getEmail(), "Помилка реєстрації",
                String.format("Сталася помилка під час реєстрації користувача: %s",
                        user.getUsername()));
    }

}

