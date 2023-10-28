package com.facebook.repository.notifications;

import com.facebook.model.notifications.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    void deleteByPostId(Long postId);

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId")
    Page<Notification> findByUserId(@Param("userId") Long userId, Pageable pageable);

    Long countByUserIdAndIsRead(Long userId, boolean isRead);

}
