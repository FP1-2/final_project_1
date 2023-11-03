package com.facebook.dto.notifications;

import com.facebook.dto.post.Author;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Об'єкт відповіді для представлення сповіщення.
 */
@Data
public class NotificationResponse {
    private Long id;
    private Long userId;
    private Author initiator;
    private Long postId;
    private String message;
    private boolean isRead;
    private String type;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}

