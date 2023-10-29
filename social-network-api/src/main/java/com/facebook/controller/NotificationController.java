package com.facebook.controller;

import com.facebook.dto.notifications.NotificationResponse;
import com.facebook.service.CurrentUserService;
import com.facebook.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final CurrentUserService currentUserService;

    @GetMapping
    public Page<NotificationResponse> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = currentUserService.getCurrentUserId();
        return notificationService.getNotificationsByUser(userId, page, size);
    }

    @PostMapping("/{notificationId}/mark-as-read")
    public void markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
    }

    @GetMapping("/unread-count")
    public Long getUnreadNotificationCount() {
        Long userId = currentUserService.getCurrentUserId();
        return notificationService.getUnreadNotificationCount(userId);
    }

}
