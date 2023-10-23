package com.facebook.dto.post;

import com.facebook.model.posts.PostType;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * Відгук на запит допису.
 *
 * <p>Цей клас використовується для представлення допису або репосту
 * у відповідь на запит. Він містить інформацію про допис, таку як автор,
 * дата створення, зображення, заголовок, текстовий контент та інші деталі.
 * Ця модель також містить списки коментарів та лайків,
 * пов'язаних із дописом.</p>
 *
 * <p>У відповідності до типу допису (оригінал або репост) деякі поля можуть бути приховані:
 * <ul>
 *     <li>Поле {@link #reposts} приховується, якщо тип допису {@link PostType#REPOST}.</li>
 *     <li>Поле {@link #originalPost} приховується, якщо тип допису {@link PostType#POST}.</li>
 * </ul>
 * </p>
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {

    private Long postId;

    private Author author;

    private LocalDateTime created_date;

    private LocalDateTime last_modified_date;

    private String imageUrl;

    private String title;

    private String body;

    private String status;

    private PostType type;

    private PostResponse originalPost;

    private List<Long> comments;

    private List<Long> likes;

    private List<Long> reposts;

}
