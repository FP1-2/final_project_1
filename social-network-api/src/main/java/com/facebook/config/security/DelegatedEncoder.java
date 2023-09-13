package com.facebook.config.security;

import static org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Configuration
public class DelegatedEncoder {

    private static final String ALGORITHM = "pbkdf2";
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 10000;
    private static final int CPU_COST = 16384;
    private static final int MEMORY_COST = 8;
    private static final int PARALLELISM = 1;
    private static final int KEY_LENGTH = 64;
    private static final int MEMORY = 65536;
    private static final int HASH_LENGTH = 32;

    @Bean
    public static PasswordEncoder build() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put(ALGORITHM, new Pbkdf2PasswordEncoder("",
                SALT_LENGTH, ITERATIONS, PBKDF2WithHmacSHA256));
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder(CPU_COST,
                MEMORY_COST, PARALLELISM, KEY_LENGTH, SALT_LENGTH));
        encoders.put("argon2", new Argon2PasswordEncoder(SALT_LENGTH,
                HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS));

        return new DelegatingPasswordEncoder(ALGORITHM, encoders);
    }
}
