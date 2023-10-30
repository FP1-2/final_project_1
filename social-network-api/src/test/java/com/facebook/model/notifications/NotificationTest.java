package com.facebook.model.notifications;

import com.facebook.model.AppUser;
import com.facebook.model.posts.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовий клас для перевірки функціональності сутності "Повідомлення".
 */
@ExtendWith(MockitoExtension.class)
class NotificationTest {

    private Notification notification;

    /**
     * Мокований об'єкт користувача.
     */
    @MockBean
    private AppUser user;

    /**
     * Мокований об'єкт ініціатора повідомлення.
     */
    @MockBean
    private AppUser initiator;

    /**
     * Мокований об'єкт публікації.
     */
    @MockBean
    private Post post;

    /**
     * Підготовка до тестування.
     */
    @BeforeEach
    void setUp() {
        notification = new Notification();
    }

    /**
     * Тест перевіряє присвоєння та отримання користувача.
     */
    @Test
    void testNotificationUser() {
        notification.setUser(user);
        assertEquals(user, notification.getUser());
    }

    /**
     * Тест перевіряє присвоєння та отримання ініціатора.
     */
    @Test
    void testNotificationInitiator() {
        notification.setInitiator(initiator);
        assertEquals(initiator, notification.getInitiator());
    }

    /**
     * Тест перевіряє присвоєння та отримання публікації.
     */
    @Test
    void testNotificationPost() {
        notification.setPost(post);
        assertEquals(post, notification.getPost());
    }

    /**
     * Тест перевіряє присвоєння та отримання повідомлення.
     */
    @Test
    void testNotificationMessage() {
        String message = "Test Message";
        notification.setMessage(message);
        assertEquals(message, notification.getMessage());
    }

    /**
     * Тест перевіряє присвоєння та перевірку статусу прочитаності повідомлення.
     */
    @Test
    void testNotificationIsRead() {
        notification.setRead(true);
        assertTrue(notification.isRead());
    }

    /**
     * Тест перевіряє присвоєння та отримання типу повідомлення.
     */
    @Test
    void testNotificationType() {
        NotificationType type = NotificationType.POST_LIKED;
        notification.setType(type);
        assertEquals(type, notification.getType());
    }

}


