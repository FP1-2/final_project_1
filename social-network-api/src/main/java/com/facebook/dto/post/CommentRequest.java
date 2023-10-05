package com.facebook.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Модель запиту для створення коментаря.
 *
 * <p>Цей клас використовується для передачі даних
 * при додаванні нового коментаря до публікації.
 * Всі поля класу підлягають валідації
 * перед опрацюванням.</p>
 *
 */
@Data
public class CommentRequest {
    /**
     * Поле не може бути null.
     */
    @NotNull(message = "postId не може бути null")
    private Long postId;

    /**
     * Зміст коментаря.
     * Поле не може бути порожнім або містити тільки пробіли.
     */
    @NotBlank(message = "Зміст коментаря не може бути порожнім")
    private String content;
}
