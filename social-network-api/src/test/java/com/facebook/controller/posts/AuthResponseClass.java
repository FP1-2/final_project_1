package com.facebook.controller.posts;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Відповідь на запит аутентифікації, що містить
 * інформацію про статус аутентифікації
 * та токен доступу.
 */
@Data
@RequiredArgsConstructor
public class AuthResponseClass {
    /** Ідентифікатор користувача. */
    private Long id;
    /** Статус відповіді (успішно чи ні). */
    private Boolean status;
    /** Повідомлення про помилку (якщо є). */
    private String error;
    /** Токен доступу для аутентифікованого користувача. */
    private String token;
    /** Список ролей користувача. */
    private List<String> role;
}
