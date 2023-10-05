package com.facebook.dto.post;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Модель відгуку для інформації про "лайк".
 *
 * <p>Цей клас представляє детальну інформацію про "лайк",
 * який користувач застосував до певної публікації.
 * Клас використовується
 * для передачі відгуків клієнтській стороні
 * після взаємодії з системою "лайків".</p>
 *
 */
@Data
public class LikeResponse {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime created_date;
}
