package com.facebook.service.notification;

import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.model.friends.FriendsStatus;
import com.facebook.model.notifications.Notification;
import com.facebook.model.notifications.NotificationType;
import com.facebook.model.posts.Post;
import com.facebook.repository.FriendsRepository;
import com.facebook.repository.notifications.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final FriendsRepository friendsRepository;

    public List<AppUser> getApprovedFriendsOfUser(Long userId) {
        return friendsRepository
                .findFriendsByUserIdAndStatus(userId, FriendsStatus.APPROVED.name())
                .stream()
                .map(Friends::getFriend)
                .toList();
    }

    public void createLikeNotification(AppUser initiator, Post post) {
        createPostNotification(initiator,
                post,
                NotificationType.POST_LIKED,
                (n, i, p) -> {
                });
    }

    public void createRepostNotification(AppUser initiator, Post post) {
        createPostNotification(initiator,
                post,
                NotificationType.POST_REPOSTED,
                (n, i, p) -> {
                });
    }

    public void createCommentNotification(AppUser initiator, Post post) {
        createPostNotification(initiator,
                post,
                NotificationType.POST_COMMENTED,
                (n, i, p) -> {
                });
    }

    public void createFriendPostNotification(AppUser initiator, Post post) {
        log.info("Post object: {}", post);
        List<AppUser> friends = getApprovedFriendsOfUser(initiator.getId());
        for (AppUser friend : friends) {
            createFriendNotification(initiator,
                    friend,
                    post,
                    NotificationType.FRIEND_POSTED,
                    (n, i, r, p) -> n.setPost(p));
        }
    }

    public void createFriendRequestNotification(AppUser initiator, AppUser receiver) {
        createFriendNotification(initiator,
                receiver,
                null,
                NotificationType.FRIEND_REQUEST, (n, i, r, p) -> {
        });
    }

    private void createPostNotification(AppUser initiator,
                                        Post post,
                                        NotificationType type,
                                        PostNotificationStrategy strategy) {
        Notification notification = new Notification();
        notification.setUser(post.getUser());
        notification.setInitiator(initiator);
        notification.setPost(post);
        notification.setType(type);
        notification.setMessage(initiator.getName() + " " + type.getDescription());
        strategy.apply(notification, initiator, post);
        notificationRepository.save(notification);
    }

    private void createFriendNotification(AppUser initiator,
                                          AppUser receiver,
                                          Post post,
                                          NotificationType type,
                                          FriendNotificationStrategy strategy) {
        Notification notification = new Notification();
        notification.setUser(receiver);
        notification.setInitiator(initiator);
        notification.setPost(post);
        notification.setType(type);
        notification.setMessage(initiator.getName() + " " + type.getDescription());
        strategy.apply(notification, initiator, receiver, post);
        notificationRepository.save(notification);
    }

}

