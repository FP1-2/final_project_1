package com.facebook.service.notification;

import com.facebook.model.AppUser;
import com.facebook.model.notifications.Notification;
import com.facebook.model.posts.Post;

/**
 * Функціональний інтерфейс для стратегії створення повідомлень пов'язаних із постами.
 * <p>
 * Цей інтерфейс визначає спосіб обробки та налаштування повідомлень, що стосуються дій з постами,
 * таких як "лайк", репост або коментування.
 * </p>
 */
@FunctionalInterface
public interface PostNotificationStrategy {

    /**
     * Застосовує стратегію до заданого повідомлення на основі ініціатора дії та конкретного поста.
     *
     * @param notification Повідомлення, до якого застосовується стратегія.
     * @param initiator    Користувач, який ініціював дію (наприклад, "лайкнув" пост).
     * @param post         Пост, до якого відноситься дія.
     */
    void apply(Notification notification, AppUser initiator, Post post);

}
