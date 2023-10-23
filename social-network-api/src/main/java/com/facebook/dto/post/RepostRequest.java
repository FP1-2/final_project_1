package com.facebook.dto.post;

import com.facebook.model.posts.PostType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Модель запиту для створення репоста.
 * <p>
 * Ця модель використовується для передачі даних між клієнтом
 * і сервером під час створення репоста від існуючого поста.
 * </p>
 */
@Data
public class RepostRequest {

    /**
     * URL зображення для репоста.
     */
    private String imageUrl;

    /**
     * Заголовок репоста.
     * <p>
     * Не обов'язкове поле, але якщо вказано, не повинно містити більше 200 символів.
     * </p>
     */
    @Size(max = 200, message = "The title of the post should not exceed 200 characters.")
    private String title;

    /**
     * Текст репоста.
     */
    private String body;

    /**
     * Тип поста. За замовчуванням встановлено {@code PostType.REPOST}.
     */
    private final PostType type = PostType.REPOST;

    /**
     * ID оригінального поста, який репоститься.
     * <p>
     * Обов'язкове поле. Вказує на пост, який користувач хоче репостити.
     * </p>
     */
    @NotNull(message = "Original post ID is required for reposting.")
    private Long originalPostId;

}

