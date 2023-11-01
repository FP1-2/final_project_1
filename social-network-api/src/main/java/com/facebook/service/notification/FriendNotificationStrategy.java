package com.facebook.service.notification;

import com.facebook.model.AppUser;
import com.facebook.model.notifications.Notification;
import com.facebook.model.posts.Post;

/**
 * Функціональний інтерфейс для стратегії створення повідомлень, пов'язаних із діями між користувачами (друзями).
 * <p>
 * Цей інтерфейс визначає спосіб обробки та налаштування повідомлень, що стосуються дій між користувачами,
 * таких як додавання в друзі або створення поста другом.
 * </p>
 */
@FunctionalInterface
public interface FriendNotificationStrategy {

    /**
     * Застосовує стратегію до заданого повідомлення на основі ініціатора,
     * отримувача та конкретного поста.
     *
     * @param notification Повідомлення, до якого застосовується стратегія.
     * @param initiator    Користувач, який ініціював дію
     *                     (наприклад, додав користувача в друзі).
     * @param receiver     Користувач, який є отримувачем дії
     *                     (наприклад, був доданий в друзі).
     * @param post         Пост, який може бути пов'язаний з дією
     *                     (наприклад, пост, створений другом).
     */
    void apply(Notification notification, AppUser initiator, AppUser receiver, Post post);

}
