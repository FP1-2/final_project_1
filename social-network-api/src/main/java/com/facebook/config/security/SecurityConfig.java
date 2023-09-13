package com.facebook.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final Environment env;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        // Якщо профіль dev - h2 консоль в браузері використовує iframe - дозволяємо.
        // Захист від CSRF атак відключаємо.
        if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {

            http.headers(headers -> headers
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            );
            http.authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/h2/**")).permitAll()
            );
        }

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/api/auth/token"),
                                AntPathRequestMatcher.antMatcher("/api/auth/signup"),
                                AntPathRequestMatcher.antMatcher("/api/auth/reset")
                        ).permitAll()
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/user/**")
                        ).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/adm/**")
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher(HttpMethod.PUT, "/api/**"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/**"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/**")
                        ).hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

