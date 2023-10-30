package com.facebook.service.notifications;

import com.facebook.dto.notifications.NotificationResponse;
import com.facebook.exception.NotFoundException;
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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Сервіс для роботи з повідомленнями в системі.
 * <p>
 * Основні компоненти та залежності сервісу:
 * <ul>
 *     <li>{@link NotificationRepository} - репозиторій для роботи з повідомленнями.</li>
 *     <li>{@link FriendsRepository} - репозиторій для роботи з друзями користувача.</li>
 *     <li>{@link ModelMapper} - інструмент для перетворення об'єктів.</li>
 * </ul>
 * </p>
 * <p>
 * Основні методи сервісу:
 * <ul>
 *     <li>{@link #getNotificationsByUserId(Long, int, int)} - отримання списку
 *                                             повідомлень для конкретного користувача.</li>
 *     <li>{@link #markNotificationAsRead(Long)} - позначення повідомлення як прочитане.</li>
 *     <li>{@link #getUnreadNotificationCount(Long)} - отримання кількості непрочитаних
 *                                                     повідомлень для користувача.</li>
 *     <li>{@link #getApprovedFriendsOfUser(Long)} - отримання списку підтверджених
 *                                                   друзів користувача.</li>
 *     <li>{@link #createLikeNotification(AppUser, Post)} - створення повідомлення
 *                                                      про "лайк" поста.</li>
 *     <li>{@link #createRepostNotification(AppUser, Post)} - створення повідомлення
 *                                                      про репост поста.</li>
 *     <li>{@link #createCommentNotification(AppUser, Post)} - створення повідомлення
 *                                                      про коментар до поста.</li>
 *     <li>{@link #createFriendPostNotification(AppUser, Post)} - створення повідомлення
 *                                                   для друзів про новий пост користувача.</li>
 *     <li>{@link #createFriendRequestNotification(AppUser, AppUser)} - створення повідомлення
 *                                                       про запит в друзі.</li>
 *     <li>{@link #createPostNotification(AppUser, Post, NotificationType, PostNotificationStrategy)}
 *             - допоміжний метод для створення повідомлення про дії, пов'язані з постами.</li>
 *     <li>{@link #createFriendNotification(AppUser, AppUser, Post, NotificationType, FriendNotificationStrategy)}
 *             - допоміжний метод для створення повідомлення про дії, пов'язані з друзями.</li>
 * </ul>
 * </p>
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final FriendsRepository friendsRepository;

    private final ModelMapper modelMapper;

    /**
     * Отримує список повідомлень для конкретного користувача.
     *
     * @param userId ID користувача.
     * @param page Номер сторінки.
     * @param size Кількість записів на сторінці.
     * @return Сторінка з повідомленнями.
     */
    public Page<NotificationResponse> getNotificationsByUserId(Long userId,
                                                             int page,
                                                             int size) {
        Pageable pageable = PageRequest.of(page,
                size, Sort.by(Sort.Direction.DESC, "createdDate"));
        return notificationRepository.findByUserId(userId, pageable)
                .map(notification -> modelMapper.map(notification,
                        NotificationResponse.class));
    }

    /**
     * Позначає повідомлення як прочитане.
     *
     * @param notificationId ID повідомлення.
     */
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository
                .findById(notificationId).orElseThrow(
                        () -> new NotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    /**
     * Отримує кількість непрочитаних повідомлень для користувача.
     *
     * @param userId ID користувача.
     * @return Кількість непрочитаних повідомлень.
     */
    public Long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }

    /**
     * Отримує список підтверджених друзів користувача.
     *
     * @param userId ID користувача.
     * @return Список друзів.
     */
    public List<AppUser> getApprovedFriendsOfUser(Long userId) {
        return friendsRepository
                .findFriendsByUserIdAndStatus(userId, FriendsStatus.APPROVED.name())
                .stream()
                .map(Friends::getFriend)
                .toList();
    }

    /**
     * Створює повідомлення про "лайк" поста.
     *
     * @param initiator Ініціатор дії (користувач, який поставив "лайк").
     * @param post      Пост, який було "лайкнуто".
     */
    public void createLikeNotification(AppUser initiator, Post post) {
        createPostNotification(initiator,
                post,
                NotificationType.POST_LIKED,
                (n, i, p) -> {
                });
    }

    /**
     * Створює повідомлення про репост поста.
     *
     * @param initiator Ініціатор дії (користувач, який зробив репост).
     * @param post      Пост, який було репостнуто.
     */
    public void createRepostNotification(AppUser initiator, Post post) {
        createPostNotification(initiator,
                post,
                NotificationType.POST_REPOSTED,
                (n, i, p) -> {
                });
    }

    /**
     * Створює повідомлення про коментар до поста.
     *
     * @param initiator Ініціатор дії (користувач, який залишив коментар).
     * @param post      Пост, до якого було залишено коментар.
     */
    public void createCommentNotification(AppUser initiator, Post post) {
        createPostNotification(initiator,
                post,
                NotificationType.POST_COMMENTED,
                (n, i, p) -> {
                });
    }

    /**
     * Створює повідомлення для друзів про новий пост користувача.
     *
     * @param initiator Ініціатор дії (користувач, який створив пост).
     * @param post      Створений пост.
     */
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

    /**
     * Створює повідомлення про запит в друзі.
     *
     * @param initiator Ініціатор дії (користувач, який відправив запит).
     * @param receiver  Користувач, якому прийшов запит в друзі.
     */
    public void createFriendRequestNotification(AppUser initiator, AppUser receiver) {
        createFriendNotification(initiator,
                receiver,
                null,
                NotificationType.FRIEND_REQUEST, (n, i, r, p) -> {
        });
    }

    /**
     * Допоміжний метод для створення повідомлення про дії, пов'язані з постами.
     *
     * @param initiator Ініціатор дії.
     * @param post      Пост, до якого відноситься дія.
     * @param type      Тип повідомлення.
     * @param strategy  Стратегія створення повідомлення.
     */
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

    /**
     * Допоміжний метод для створення повідомлення про дії, пов'язані з друзями.
     *
     * @param initiator Ініціатор дії.
     * @param receiver  Користувач, якому прийшов запит в друзі або повідомлення.
     * @param post      Пост, до якого відноситься дія (якщо є).
     * @param type      Тип повідомлення.
     * @param strategy  Стратегія створення повідомлення.
     */
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

