package com.facebook.dto.notifications;

import com.facebook.dto.post.Author;
import com.facebook.model.notifications.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationSqlResult {
    private Long id;
    private Long userId;
    private String message;
    private boolean isRead;
    private NotificationType type;
    private Long postId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Author initiator;

}
