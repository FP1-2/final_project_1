package com.facebook.config.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;


public class CacheStore<T> {
    private final Cache<String, T> cache;
    public CacheStore(int expiryDuration, TimeUnit timeUnit) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiryDuration, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }
    public T get(String key) {
        return cache.getIfPresent(key);
    }
    public void add(String key, T value) {
       cache.put(key, value);
    }
    public void removeToken(String email) {
        cache.invalidate(email);
    }
}
