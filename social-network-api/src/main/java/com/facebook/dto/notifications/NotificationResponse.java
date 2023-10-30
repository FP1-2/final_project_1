package com.facebook.dto.notifications;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private Long userId;
    private Long initiatorId;
    private Long postId;
    private String message;
    private boolean isRead;
    private String type;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}

