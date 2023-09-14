package com.facebook.dto.appuser;

public record LoginRequest(String username, String password, Boolean rememberMe) {}
