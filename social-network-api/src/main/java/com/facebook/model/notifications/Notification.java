package com.facebook.model.notifications;

import com.facebook.model.AbstractEntity;
import com.facebook.model.AppUser;
import com.facebook.model.posts.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Клас, що представляє собою повідомлення в системі. Основні поля класу:
 * <ul>
 *     <li>{@link #user} - Користувач, для якого призначене повідомлення.</li>
 *     <li>{@link #initiator} - Ініціатор повідомлення (наприклад,
 *                         користувач, який залишив коментар).</li>
 *     <li>{@link #post} - Пост, до якого відноситься повідомлення.</li>
 *     <li>{@link #message} - Текст повідомлення.</li>
 *     <li>{@link #isRead} - Чи було прочитано повідомлення користувачем.</li>
 *     <li>{@link #type} - Тип повідомлення.</li>
 * </ul>
 */
@Data
@Entity
@Table(name = "notifications")
@EqualsAndHashCode(callSuper = true)
public class Notification extends AbstractEntity {

    /**
     * Користувач, для якого призначене повідомлення.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            nullable = false)
    private AppUser user;

    /**
     * Ініціатор повідомлення (наприклад, користувач, який залишив коментар).
     */
    @ManyToOne
    @JoinColumn(name = "initiator_id",
            referencedColumnName = "id")
    private AppUser initiator;

    /**
     * Пост, до якого відноситься повідомлення.
     */
    @ManyToOne
    @JoinColumn(name = "post_id",
            referencedColumnName = "id")
    private Post post;

    /**
     * Текст повідомлення.
     */
    private String message;

    /**
     * Чи було прочитано повідомлення користувачем.
     */
    private boolean isRead = false;

    /**
     * Тип повідомлення {@link NotificationType}.
     */
    @Enumerated(EnumType.STRING)
    private NotificationType type;

}


