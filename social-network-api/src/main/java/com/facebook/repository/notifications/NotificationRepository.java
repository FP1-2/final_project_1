package com.facebook.repository.notifications;

import com.facebook.dto.notifications.NotificationSqlResult;
import com.facebook.model.notifications.Notification;
import com.facebook.model.notifications.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи з повідомленнями.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Базовий запит SQL для отримання даних про повідомлення.
     * Цей запит використовується як основа і потребує додавання конкретних умов WHERE для завершення запиту.
     * Використовується в методах {@link NotificationRepository#findByUserId(Long, Pageable)}
     * та {@link NotificationRepository#findByNotificationId(Long)}.
     */
    String BASE_NOTIFICATION_QUERY = """
                SELECT new com.facebook.dto.notifications.NotificationSqlResult(
                       n.id,
                       n.user.id,
                       n.message,
                       n.isRead,
                       n.type,
                       n.post.id,
                       n.createdDate,
                       n.lastModifiedDate,
                       new com.facebook.dto.post.Author(
                           i.id,
                           i.name,
                           i.surname,
                           i.username,
                           i.avatar
                       )
                   )
                   FROM Notification n
                   LEFT JOIN n.initiator i
            """;

    /**
     * Знаходить всі повідомлення для певного користувача.
     *
     * @param userId   Ідентифікатор користувача.
     * @param pageable Параметри пагінації.
     * @return Сторінка з повідомленнями.
     */
    @Query(BASE_NOTIFICATION_QUERY + " WHERE n.user.id = :userId")
    Page<NotificationSqlResult> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * Знаходить запис про повідомлення за ідентифікатором.
     *
     * @param notificationId Ідентифікатор повідомлення, яке потрібно знайти.
     * @return Опціональний результат, який містить {@link NotificationSqlResult}, якщо повідомлення з таким ідентифікатором існує.
     */
    @Query(BASE_NOTIFICATION_QUERY + " WHERE n.id = :notificationId")
    Optional<NotificationSqlResult> findByNotificationId(@Param("notificationId") Long notificationId);

    /**
     * Видаляє всі повідомлення, пов'язані з певним постом.
     *
     * @param postId Ідентифікатор поста.
     */
    void deleteByPostId(Long postId);

    /**
     * Підраховує кількість прочитаних або непрочитаних повідомлень для певного користувача.
     *
     * @param userId Ідентифікатор користувача.
     * @param isRead Статус прочитаності: true - прочитані, false - непрочитані.
     * @return Кількість повідомлень.
     */
    Long countByUserIdAndIsRead(Long userId, boolean isRead);

    /**
     * Видаляє всі сповіщення, які відповідають заданим критеріям.
     * Цей метод видаляє сповіщення, де ініціатор та користувач відповідають
     * заданим ідентифікаторам та типу сповіщення. Видалення відбувається для випадків,
     * коли або ініціатор, або користувач є ініціатором або отримувачем сповіщення.
     *
     * @param initiatorId ідентифікатор ініціатора сповіщення
     * @param userId ідентифікатор користувача, якому було надіслано сповіщення
     * @param type тип сповіщення, яке потрібно видалити
     */
    @Modifying
    @Query("""
        delete from Notification n
        where (n.initiator.id = :initiatorId and n.user.id = :userId and n.type = :type)
        or (n.initiator.id = :userId and n.user.id = :initiatorId and n.type = :type)
        """)
    void deleteByInitiatorAndUserAndType(Long initiatorId, Long userId, NotificationType type);

    List<Notification> findAllByPostIdIn(List<Long> postIds);

}
