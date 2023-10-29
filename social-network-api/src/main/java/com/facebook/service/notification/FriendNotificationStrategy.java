package com.facebook.service.notification;

import com.facebook.model.AppUser;
import com.facebook.model.notifications.Notification;
import com.facebook.model.posts.Post;

@FunctionalInterface
public interface FriendNotificationStrategy {
    void apply(Notification notification, AppUser initiator, AppUser receiver, Post post);
}
