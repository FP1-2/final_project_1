package com.facebook.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.facebook.dto.notifications.NotificationSqlResult;
import com.facebook.dto.post.Author;
import com.facebook.model.notifications.NotificationType;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

/**
 * Тестовий клас для {@link NotificationSqlResult}.
 * Перевіряє коректність роботи конструктора та методів доступу (getters/setters).
 */
class NotificationSqlResultTest {

    /**
     * Тестує об'єкт {@link NotificationSqlResult} .
     */
    @Test
    void testNotificationSqlResultGettersAndSetters() {
        // Підготовка тестових даних
        Long id = 1L;
        Long userId = 2L;
        String message = "Test Message";
        boolean isRead = true;
        NotificationType type = NotificationType.POST_COMMENTED;
        Long postId = 3L;
        LocalDateTime createdDate = LocalDateTime.now();
        LocalDateTime lastModifiedDate = LocalDateTime.now();
        Author initiator = new Author(4L,
                "John",
                "Doe",
                "johndoe",
                "avatarUrl");

        // Створення екземпляра DTO
        NotificationSqlResult notificationSqlResult = new NotificationSqlResult(
                id, userId, message, isRead, type, postId, createdDate, lastModifiedDate, initiator);

        // Перевірка значень через getters
        assertEquals(id, notificationSqlResult.getId());
        assertEquals(userId, notificationSqlResult.getUserId());
        assertEquals(message, notificationSqlResult.getMessage());
        assertEquals(isRead, notificationSqlResult.isRead());
        assertEquals(type, notificationSqlResult.getType());
        assertEquals(postId, notificationSqlResult.getPostId());
        assertEquals(createdDate, notificationSqlResult.getCreatedDate());
        assertEquals(lastModifiedDate, notificationSqlResult.getLastModifiedDate());
        assertEquals(initiator, notificationSqlResult.getInitiator());
    }

}

