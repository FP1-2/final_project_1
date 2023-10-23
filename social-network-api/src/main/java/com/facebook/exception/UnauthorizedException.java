package com.facebook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Виняток, який використовується для позначення ситуацій, коли користувач намагається
 * виконати дію без відповідних прав.
 *
 * <p>Цей виняток пов'язаний із статус-кодом HTTP 401 (Unauthorized).</p>
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

}

