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

@Data
@Entity
@Table(name = "notifications")
@EqualsAndHashCode(callSuper = true)
public class Notification extends AbstractEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "initiator_id",
            referencedColumnName = "id")
    private AppUser initiator;

    @ManyToOne
    @JoinColumn(name = "post_id",
            referencedColumnName = "id")
    private Post post;

    private String message;

    private boolean isRead = false;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

}

