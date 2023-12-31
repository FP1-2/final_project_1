package com.facebook.config.security;

import com.facebook.service.AppUserService;
import com.facebook.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
    private final JwtTokenService tokenService;
    private final AppUserService appUserService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain chain) throws ServletException, IOException {
        if (isRequestForProtectedResource(request)) {
            try {
                processTokenAndSetupAuthentication(request);
                log.info("Roles: {}",
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication());
                chain.doFilter(request, response);
            } catch (UsernameNotFoundException ex) {
                log.info("Username not found; {}",
                        ex.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("User not found");
            } catch (Exception ex) {
                log.error("Error occurred: {}",
                        ex.getMessage());
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean isRequestForProtectedResource(HttpServletRequest request) {
        String path = request.getRequestURI();
        return switch (path) {
            case "/api/auth/signup",
                    "/api/auth/confirm/**",
                    "/api/auth/token",
                    "/api/users/reset-password/**",
                    "/api/users/update-password/**",
                    "/api-docs/**",
                    "/swagger*/**",
                    "/error",
                    "/ws/**",
                    "/h2" -> false;
            default -> true;
        };
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest rq) {
        return Optional.ofNullable(rq.getHeader(AUTH_HEADER))
                .filter(h -> h.startsWith(BEARER))
                .map(h -> h.substring(BEARER.length()));
    }

    private void processTokenAndSetupAuthentication(HttpServletRequest request) {
        String token = extractTokenFromRequest(request).orElse(null);

        if (token != null) {
            Optional<Integer> maybeId = tokenService.parseToken(token);
            maybeId.map(id -> AppUserDetails.of(id,
                     appUserService.findRolesById((long) id),
                     appUserService.findUsernameById((long) id),
                     appUserService.findPasswordById((long) id),

                     // Тут встановлюємо термін дії облікового запису
                     // використовуючи час життя токена,
                     // правильно буде створити поле в AppUser
                     // та відповідні методи управління
                     // та зробити доступ адміну відповідними контролерами
                     tokenService.getExpirationDateFromToken(token)
                    ))
                    .map(ud -> new UsernamePasswordAuthenticationToken(ud,
                            null, ud.getAuthorities()))
                    .ifPresentOrElse(at -> {
                        at.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(at);
                    }, () -> log.error("Failed to process setup authentication in filter"));
        }
    }
}
