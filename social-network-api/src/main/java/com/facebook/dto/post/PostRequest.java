package com.facebook.dto.post;

import com.facebook.model.posts.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Модель запиту для створення або оновлення поста.
 * <p>
 * Ця модель використовується для передачі даних між клієнтом
 * і сервером під час створення нового поста
 * або його редагування.
 * </p>
 */
@Data
public class PostRequest {

    /**
     * URL зображення для поста.
     */
    private String imageUrl;

    /**
     * Заголовок поста.
     * <p>
     * Обов'язкове поле. Не може бути порожнім і має містити не більше 200 символів.
     */
    @NotBlank(message = "The post title cannot be empty.")
    @Size(max = 200, message = "The title of the post should not exceed 200 characters.")
    private String title;

    /**
     * Текст поста.
     * <p>
     * Обов'язкове поле. Не може бути порожнім.
     */
    @NotBlank(message = "The text of the post cannot be empty")
    private String body;

    /**
     * Тип поста. За замовчуванням встановлено {@code PostType.POST}.
     */
    private final PostType type = PostType.POST;

    @Override
    public String toString() {
        return """
                PostRequest{imageUrl='%s', title='%s', body='%s', type=%s}"""
                .formatted(imageUrl, title, body, type);
    }

}
