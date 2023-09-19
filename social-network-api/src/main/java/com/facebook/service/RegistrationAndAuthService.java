package com.facebook.service;

import com.facebook.config.cache.CacheStore;
import com.facebook.controller.SignupResponse;
import com.facebook.dto.appuser.AppUserRequest;
import com.facebook.dto.appuser.CreateAppUserResponse;
import com.facebook.dto.appuser.LoginRequest;
import com.facebook.dto.appuser.LoginResponse;
import com.facebook.exception.EmailSendingException;
import com.facebook.exception.InvalidTokenException;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;

import java.util.Optional;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Log4j2
@Service
public class RegistrationAndAuthService {

    private final AppUserService appUserService;
    private final AppUserFacade appUserFacade;
    private final PasswordEncoder passwordEncoder;
    private final EmailHandlerService emailHandler;
    private final JwtTokenService tokenService;
    private final CacheStore<String> registrationAndAuthTokenCache;
    private final CacheStore<AppUserRequest> appUserRequestCache;

    @Autowired
    public RegistrationAndAuthService(AppUserService appUserService,
                                      AppUserFacade appUserFacade,
                                      PasswordEncoder passwordEncoder,
                                      EmailHandlerService emailHandler,
                                      JwtTokenService tokenService,
                                      @Qualifier("registrationAndAuthTokenCache")
                                      CacheStore<String> registrationAndAuthTokenCache,
                                      @Qualifier("appUserRequestCache")
                                      CacheStore<AppUserRequest> appUserRequestCache) {
        this.appUserService = appUserService;
        this.appUserFacade = appUserFacade;
        this.passwordEncoder = passwordEncoder;
        this.emailHandler = emailHandler;
        this.tokenService = tokenService;
        this.registrationAndAuthTokenCache = registrationAndAuthTokenCache;
        this.appUserRequestCache = appUserRequestCache;
    }

    private static final String BASE_URL = "http://localhost:9000";

    public void confirmRegistration(String token, String email) {
        // Отримати дані з кешу та перевірити їх
        AppUserRequest appUserRequest = Optional.ofNullable(appUserRequestCache.get(token))
                .filter(request -> request.getEmail().equals(email))
                .orElseThrow(() -> new InvalidTokenException("Invalid token or user not found"));

        // Конвертувати дані та зберегти їх у базу даних
        AppUser user = appUserFacade.convertToAppUser(appUserRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new String[]{"USER"});

        Optional<AppUser> savedUser = appUserService.save(user);

        // Якщо користувача успішно збережено, надіслати листа
        savedUser.ifPresentOrElse(
                saved -> {
                    try {
                        sendWelcomeEmail(saved);
                    } catch (EmailSendingException e) {
                        log.error("An error occurred while "
                                + "sending the welcome email ", e);
                    }
                },
                () -> {
                    throw new DataIntegrityViolationException("An error occurred "
                            + "during user registration");
                }
        );

        // Видалити дані з кешу, оскільки реєстрація підтверджена
        appUserRequestCache.remove(token);
        registrationAndAuthTokenCache.remove(token);
    }

    public SignupResponse createAppUser(AppUserRequest appUserRequest) {
        String token = UUID.randomUUID().toString();
        appUserRequestCache.add(token, appUserRequest);

        String url = BASE_URL + "/api/auth/confirm?token="
                + token + "&em=" + appUserRequest.getEmail();
        registrationAndAuthTokenCache.add(token, url);

        try {
            sendRegistrationEmail(appUserRequest.getEmail(), url);
            return CreateAppUserResponse.ok("Registration is almost complete. "
                    + "Check your email for confirmation.");
        } catch (Exception e) {
            log.error("An error occurred when sending "
                    + "an email to confirm registration ", e);
            return CreateAppUserResponse.error("There was an error sending "
                            + "the confirmation email.",
                    "The error is unknown.");
        }

    }

    private void sendRegistrationEmail(String email, String url) throws EmailSendingException {
        emailHandler.sendEmail(email, "Confirm registration",
                String.format("Please click the following link "
                        + "to confirm your registration: %s", url));
    }

    public LoginResponse handleLogin(LoginRequest rq) {
        return appUserService.findByUsername(rq.username())
                .filter(u -> passwordEncoder.matches(rq.password(), u.getPassword()))
                .map(u -> LoginResponse.ok(u.getId(),
                        tokenService.generateToken(Math.toIntExact(u.getId())), u.getRoles()))
                .orElseThrow(() -> new IllegalArgumentException("wrong user/password combination"));
    }

    private void sendWelcomeEmail(AppUser user) throws EmailSendingException {
        emailHandler.sendEmail(user.getEmail(), "Registration is complete.",
                String.format("Welcome, %s! Your registration was successful.",
                        user.getUsername()));
    }

}

