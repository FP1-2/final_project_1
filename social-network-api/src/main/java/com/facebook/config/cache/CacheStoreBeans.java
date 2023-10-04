package com.facebook.config.cache;

import com.facebook.dto.appuser.AppUserRequest;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheStoreBeans {
    @Bean("resetPasswordTokenCache")
    public CacheStore<String> resetPasswordTokenCache() {
        return new CacheStore<>(15, TimeUnit.MINUTES);
    }

    @Bean("registrationAndAuthTokenCache")
    public CacheStore<String> registrationAndAuthTokenCache() {
        return new CacheStore<>(15, TimeUnit.MINUTES);
    }

    @Bean("appUserRequestCache")
    public CacheStore<AppUserRequest> appUserRequestCache() {
        return new CacheStore<>(15, TimeUnit.MINUTES);
    }
}
