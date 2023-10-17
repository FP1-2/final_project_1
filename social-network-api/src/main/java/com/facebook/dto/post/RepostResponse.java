package com.facebook.dto.post;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Модель відгуку для інформації про репост.
 *
 * <p>Цей клас представляє детальну інформацію про репост,
 * який користувач здійснив для певної публікації.
 * Клас використовується
 * для передачі відгуків клієнтській стороні
 * після взаємодії з системою репостів.</p>
 *
 */
@Data
public class RepostResponse {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime created_date;
}
