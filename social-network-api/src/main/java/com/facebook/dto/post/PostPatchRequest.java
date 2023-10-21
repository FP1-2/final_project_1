package com.facebook.dto.post;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO, що представляє запит на часткове оновлення існуючого поста/репоста.
 * Дозволяє вносити часткові зміни до деталей поста без необхідності
 * надання всіх полів.
 *
 * <p>Кожне надане поле буде використано для оновлення відповідного
 * атрибуту поста, інші поля залишаться без змін.</p>
 */
@Data
public class PostPatchRequest {

    private String imageUrl;

    @Size(max = 200, message = "The title of the post should not exceed 200 characters.")
    private String title;

    private String body;

}
