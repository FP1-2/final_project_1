package com.facebook.service;

import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.model.friends.FriendsStatus;
import com.facebook.model.notifications.Notification;
import com.facebook.model.posts.Post;
import com.facebook.repository.FriendsRepository;
import com.facebook.repository.notifications.NotificationRepository;
import com.facebook.service.notifications.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Клас для юніт-тестування {@link NotificationService}.
 * Перевіряє коректність створення повідомлень різних типів.
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private FriendsRepository friendsRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NotificationService notificationService;

    private AppUser user;

    private AppUser initiator;

    private Post post;

    // ... ініціалізація об'єктів
    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setId(1L);
        user.setName("Іван");

        initiator = new AppUser();
        initiator.setId(2L);
        initiator.setName("Ольга");

        post = new Post();
        post.setId(3L);
        post.setUser(user);
    }

    /**
     * Тестує створення повідомлення про "лайк" поста.
     */
    @Test
    void testCreateLikeNotification() {
        notificationService.createLikeNotification(initiator, post);

        verify(notificationRepository).save(any(Notification.class));
    }

    /**
     * Тестує створення повідомлення про репост поста.
     */
    @Test
    void testCreateRepostNotification() {
        notificationService.createRepostNotification(initiator, post);

        verify(notificationRepository).save(any(Notification.class));
    }

    /**
     * Тестує створення повідомлення про коментар до поста.
     */
    @Test
    void testCreateCommentNotification() {
        notificationService.createCommentNotification(initiator, post);

        verify(notificationRepository).save(any(Notification.class));
    }

    /**
     * Тестує створення повідомлення для друзів про новий пост користувача.
     */
    @Test
    void testCreateFriendPostNotification() {
        List<AppUser> friends = List.of(user);
        when(friendsRepository.findFriendsByUserIdAndStatus(initiator.getId(),
                FriendsStatus.APPROVED.name())).thenReturn(List.of(new Friends()));

        notificationService.createFriendPostNotification(initiator, post);

        verify(notificationRepository, times(friends.size())).save(any(Notification.class));
    }

    /**
     * Тестує створення повідомлення про запит у друзі.
     */
    @Test
    void testCreateFriendRequestNotification() {
        notificationService.createFriendRequestNotification(initiator, user);

        verify(notificationRepository).save(any(Notification.class));
    }

}

