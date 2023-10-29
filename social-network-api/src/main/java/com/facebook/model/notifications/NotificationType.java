package com.facebook.model.notifications;

import lombok.Getter;

@Getter
public enum NotificationType {
    POST_LIKED("Liked my post"),
    POST_REPOSTED("Reposted my post"),
    POST_COMMENTED("Commented on my post"),
    FRIEND_POSTED("A friend made a post"),
    FRIEND_REQUEST("Added me as a friend");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

}
