package com.facebook.dto.post;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Відповідь на коментар.
 *
 * <p>Цей клас представляє собою модель
 * відповіді на коментар.
 * Він містить інформацію про коментар,
 * таку як ID користувача, ID публікації,
 * зміст коментаря та дату створення.</p>
 *
 */
@Data
public class CommentResponse {
    private Long id;
    private Long userId;
    private Long postId;
    private String content;
    private LocalDateTime created_date;
}
