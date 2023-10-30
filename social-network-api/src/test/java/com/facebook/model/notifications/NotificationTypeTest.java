package com.facebook.model.notifications;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Тестовий клас для перевірки значень переліку "Тип Повідомлення".
 */
public class NotificationTypeTest {

    /**
     * Тест перевіряє відповідність описів значенням переліку "Тип Повідомлення".
     */
    @Test
    public void enumValuesTest() {
        assertSame("Liked my post", NotificationType.POST_LIKED.getDescription());
        assertSame("Reposted my post", NotificationType.POST_REPOSTED.getDescription());
        assertSame("Commented on my post", NotificationType.POST_COMMENTED.getDescription());
        assertSame("A friend made a post", NotificationType.FRIEND_POSTED.getDescription());
        assertSame("Added me as a friend", NotificationType.FRIEND_REQUEST.getDescription());
    }

}


