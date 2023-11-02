package com.facebook.controller;

import com.facebook.dto.notifications.NotificationResponse;
import com.facebook.service.CurrentUserService;
import com.facebook.service.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контролер для обробки HTTP-запитів, пов'язаних з повідомленнями.
 * <p>
 * Основні залежності контролера:
 * <ul>
 *     <li>{@link NotificationService} - сервіс для роботи з повідомленнями.</li>
 *     <li>{@link CurrentUserService} - сервіс для отримання інформації про поточного користувача.</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final CurrentUserService currentUserService;

    /**
     * Отримання списку повідомлень для поточного користувача.
     *
     * @param page номер сторінки (за замовчуванням 0).
     * @param size кількість повідомлень на сторінці (за замовчуванням 10).
     * @return сторінка з повідомленнями.
     */
    @GetMapping
    public Page<NotificationResponse> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = currentUserService.getCurrentUserId();
        return notificationService.getNotificationsByUserId(userId, page, size);
    }

    /**
     * Отримання одного повідомлення за ID
    */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long id) {
        Long userId = currentUserService.getCurrentUserId();
        NotificationResponse notification = notificationService.getNotificationById(id, userId);
        return ResponseEntity.ok(notification);
    }

    /**
     * Позначення конкретного повідомлення як прочитане.
     *
     * @param notificationId ID повідомлення, яке потрібно позначити як прочитане.
     */
    @PostMapping("/{notificationId}/mark-as-read")
    public void markNotificationAsRead(@PathVariable Long notificationId) {
        Long userId = currentUserService.getCurrentUserId();
        notificationService.markNotificationAsRead(notificationId, userId);
    }

    /**
     * Отримання кількості непрочитаних повідомлень для поточного користувача.
     *
     * @return кількість непрочитаних повідомлень.
     */
    @GetMapping("/unread-count")
    public Long getUnreadNotificationCount() {
        Long userId = currentUserService.getCurrentUserId();
        return notificationService.getUnreadNotificationCount(userId);
    }

}