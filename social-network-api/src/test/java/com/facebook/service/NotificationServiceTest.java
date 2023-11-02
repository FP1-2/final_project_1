package com.facebook.service;

import com.facebook.dto.notifications.NotificationResponse;
import com.facebook.dto.notifications.NotificationSqlResult;
import com.facebook.dto.post.Author;
import com.facebook.exception.NotFoundException;
import com.facebook.exception.UnauthorizedException;
import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.model.friends.FriendsStatus;
import com.facebook.model.notifications.Notification;
import com.facebook.model.posts.Post;
import com.facebook.repository.FriendsRepository;
import com.facebook.repository.notifications.NotificationRepository;
import com.facebook.service.notifications.NotificationService;
import com.facebook.model.notifications.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    /**
     * Перевіряє, що метод getNotificationById повертає коректний об'єкт NotificationResponse,
     * коли повідомлення знайдено і користувач має до нього доступ.
     */
    @Test
    void testGetNotificationById() {
        // Підготовка даних
        Long notificationId = 1L;
        Long userId = user.getId();
        NotificationSqlResult notificationSqlResult = new NotificationSqlResult(
                notificationId,
                userId,
                "Test message",
                false,
                NotificationType.POST_LIKED,
                post.getId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Author(initiator.getId(),
                        initiator.getName(),
                        initiator.getSurname(),
                        initiator.getUsername(),
                        initiator.getAvatar())
        );
        NotificationResponse expectedResponse = new NotificationResponse();
        expectedResponse.setMessage("Test message");

        // Налаштування поведінки моків
        when(notificationRepository.findByNotificationId(notificationId))
                .thenReturn(Optional.of(notificationSqlResult));
        when(modelMapper.map(notificationSqlResult, NotificationResponse.class))
                .thenReturn(expectedResponse);

        // Виконання тесту
        NotificationResponse actualResponse = notificationService
                .getNotificationById(notificationId, userId);

        // Перевірка результатів
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        verify(notificationRepository).findByNotificationId(notificationId);
        verify(modelMapper).map(notificationSqlResult, NotificationResponse.class);
    }

    /**
     * Перевіряє, що метод getNotificationById кидає {@link  UnauthorizedException},
     * коли користувач намагається отримати доступ до повідомлення, яке йому не належить.
     */
    @Test
    void testGetNotificationByIdUnauthorized() {
        // Підготовка даних
        Long notificationId = 1L;
        Long wrongUserId = 99L; // ID, який не відповідає користувачу повідомлення
        NotificationSqlResult notificationSqlResult = new NotificationSqlResult(
                notificationId,
                user.getId(),
                "Test message",
                false,
                NotificationType.POST_LIKED,
                post.getId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Author(initiator.getId(),
                        initiator.getName(),
                        initiator.getSurname(),
                        initiator.getUsername(),
                        initiator.getAvatar())
        );

        // Налаштування поведінки моків
        when(notificationRepository.findByNotificationId(notificationId))
                .thenReturn(Optional.of(notificationSqlResult));

        // Виконання тесту та перехоплення виключення
        assertThrows(UnauthorizedException.class,
                () -> notificationService
                .getNotificationById(notificationId, wrongUserId));

        // Перевірка, що метод репозиторію був викликаний
        verify(notificationRepository).findByNotificationId(notificationId);
        verify(modelMapper, never()).map(any(), any());
    }

    /**
     * Перевіряє, що метод getNotificationById кидає {@link  NotFoundException},
     * коли повідомлення з вказаним ідентифікатором не знайдено.
     */
    @Test
    void testGetNotificationByIdNotFound() {
        // Підготовка даних
        Long notificationId = 1L;
        Long userId = user.getId();

        // Налаштування поведінки моків
        when(notificationRepository.findByNotificationId(notificationId))
                .thenReturn(Optional.empty());

        // Виконання тесту та перехоплення виключення
        assertThrows(NotFoundException.class,
                () -> notificationService
                .getNotificationById(notificationId, userId));

        // Перевірка, що метод репозиторію був викликаний
        verify(notificationRepository).findByNotificationId(notificationId);
        verify(modelMapper, never()).map(any(), any());
    }

}

