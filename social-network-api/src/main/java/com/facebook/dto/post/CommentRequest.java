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
    @NotNull(message = "postId cannot be null.")
    private Long postId;

    /**
     * Зміст коментаря.
     * Поле не може бути порожнім або містити тільки пробіли.
     */
    @NotBlank(message = "Comment content cannot be empty.")
    private String content;

    private Long groupId;

}
