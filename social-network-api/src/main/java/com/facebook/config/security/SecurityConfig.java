package com.facebook.config.security;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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

    private final JwtFilter jwtFilter;

    private final Environment env;

    @Value("${frontend.url}")
    private String clientUrl;

    @Value("${frontend.urlPreview}")
    private String urlPreview;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //дозволяти основні запити із програми на clientUrl
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOriginPatterns(Arrays.asList(clientUrl, urlPreview));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
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
                                AntPathRequestMatcher.antMatcher("/api/users/update-password/**"),
                                AntPathRequestMatcher.antMatcher("/ws/**"),
                                AntPathRequestMatcher.antMatcher("/api/users/update-password/**"),
                                AntPathRequestMatcher.antMatcher("/api-docs/**"),
                                AntPathRequestMatcher.antMatcher("/swagger*/**"),
                                AntPathRequestMatcher.antMatcher("/actuator*/**"),
                                AntPathRequestMatcher.antMatcher("/error")
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

