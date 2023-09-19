package com.facebook.config.security;

import java.util.Arrays;
import java.util.Collections;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String API = "/api/**";

    private static final String ADMIN = "ADMIN";

    private static final String USER = "USER";

    private final JwtFilter jwtFilter;

    private final Environment env;

    @Value("${frontend.url:http://localhost:3000}")
    private String clientUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //дозволяти основні запити із програми на FRONTEND_URL
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList(clientUrl));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
            configuration.setAllowCredentials(true);
            return configuration;
        }));

        // Захист від CSRF атак відключаємо.
        http.csrf(AbstractHttpConfigurer::disable);

        // Якщо профіль dev - h2 консоль в браузері використовує iframe - дозволяємо.
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
                                AntPathRequestMatcher.antMatcher("/api/auth/confirm/**"),
                                AntPathRequestMatcher.antMatcher("/api/users/reset-password/**"),
                                AntPathRequestMatcher.antMatcher("/api/users/update-password/**")
                        ).permitAll()
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/user/**")
                        ).hasAnyRole(ADMIN, USER)
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/adm/**")
                        ).hasRole(ADMIN)
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher(HttpMethod.PUT, API),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, API),
                                AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, API)
                        ).hasAnyRole(ADMIN, USER)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

