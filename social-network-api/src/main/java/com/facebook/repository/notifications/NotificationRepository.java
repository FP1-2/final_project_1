package com.facebook.repository.notifications;

import com.facebook.model.notifications.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Репозиторій для роботи з повідомленнями.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Видаляє всі повідомлення, пов'язані з певним постом.
     *
     * @param postId Ідентифікатор поста.
     */
    void deleteByPostId(Long postId);

    /**
     * Знаходить всі повідомлення для певного користувача.
     *
     * @param userId Ідентифікатор користувача.
     * @param pageable Параметри пагінації.
     * @return Сторінка з повідомленнями.
     */
    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId")
    Page<Notification> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * Підраховує кількість прочитаних або непрочитаних повідомлень для певного користувача.
     *
     * @param userId Ідентифікатор користувача.
     * @param isRead Статус прочитаності: true - прочитані, false - непрочитані.
     * @return Кількість повідомлень.
     */
    Long countByUserIdAndIsRead(Long userId, boolean isRead);

    List<Notification> findAllByPostIdIn(List<Long> postIds);

}
