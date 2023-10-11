package com.facebook.controller.posts;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Об'єкт запиту для аутентифікації користувача.
 */
@Data
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}

