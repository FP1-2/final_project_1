package com.facebook.repository.notifications;

import com.facebook.model.notifications.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    void deleteByPostId(Long postId);

}
