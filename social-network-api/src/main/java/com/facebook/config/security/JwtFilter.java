package com.facebook.config.security;

import com.facebook.service.AppUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
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
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        if (isRequestForProtectedResource(request)) {
            try {
                processTokenAndSetupAuthentication(request);
                log.info("Roles: {}",
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication());
                chain.doFilter(request, response);
            } catch (UsernameNotFoundException ex) {
                log.info("UsernameNotFoundException: Username not found; {}",
                        ex.getMessage());
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
               "/api/auth/token",
               "/api/auth/reset",
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
        extractTokenFromRequest(request)
                .flatMap(tokenService::parseToken)
                .map(id -> new JwtAppUserDetails(id, appUserService))
                .map(ud -> new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities()))
                .ifPresentOrElse(at -> {
                    at.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(at);
                }, () -> log.error("Failed to process setup authentication in filter"));
    }
}
