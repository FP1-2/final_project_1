package com.facebook.config.security;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Log4j2
@Service
@PropertySource("classpath:jwt.properties")
public class JwtTokenService {

    @Value("${jwt.secret}")
    private String secret;

    private static final Long day = 60 * 60 * 24 * 1000L;

    private static final Long week = day * 7;

    public String generateToken(Integer userId, boolean rememberMe) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + (rememberMe ? week : day));
        return Jwts.builder()
                .setSubject(userId.toString()) // ANY string / JSON / whatever
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateToken(Integer userId) {
        return generateToken(userId, false);
    }

    private Optional<Jws<Claims>> parseTokenToClaims(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token));
        } catch (JwtException x) {
            log.error("something went wrong with token", x);
            return Optional.empty();
        }
    }

    public Optional<Integer> parseToken(String token) {
        return parseTokenToClaims(token)
                .map(Jwt::getBody)
                .map(Claims::getSubject)
                .map(Integer::parseInt);
    }
}
