package com.facebook.model.notifications;

import lombok.Getter;

/**
 * Перелік типів повідомлень в системі:
 * <ul>
 *     <li>{@link #POST_LIKED} - Повідомлення про те,
 *                      що мій пост отримав "лайк".</li>
 *     <li>{@link #POST_REPOSTED} - Повідомлення про те,
 *                      що мій пост було репостнуто.</li>
 *     <li>{@link #POST_COMMENTED} - Повідомлення про те,
 *                      що на мій пост було залишено коментар.</li>
 *     <li>{@link #FRIEND_POSTED} - Повідомлення про те,
 *                      що мій друг створив новий пост.</li>
 *     <li>{@link #FRIEND_REQUEST} - Повідомлення про те,
 *                      що мене додали до друзів.</li>
 * </ul>
 */
@Getter
public enum NotificationType {

    /**
     * Повідомлення про те, що мій пост отримав "лайк".
     */
    POST_LIKED("Liked my post"),

    /**
     * Повідомлення про те, що мій пост було репостнуто.
     */
    POST_REPOSTED("Reposted my post"),

    /**
     * Повідомлення про те, що на мій пост було залишено коментар.
     */
    POST_COMMENTED("Commented on my post"),

    /**
     * Повідомлення про те, що мій друг створив новий пост.
     */
    FRIEND_POSTED("A friend made a post"),

    /**
     * Повідомлення про те, що мене додали до друзів.
     */
    FRIEND_REQUEST("Added me as a friend");

    /**
     * Опис типу повідомлення.
     */
    private final String description;

    /**
     * Конструктор для ініціалізації типу повідомлення з його описом.
     *
     * @param description Опис типу повідомлення.
     */
    NotificationType(String description) {
        this.description = description;
    }

}

