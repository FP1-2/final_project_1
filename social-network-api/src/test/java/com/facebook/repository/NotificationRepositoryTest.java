package com.facebook.repository;

import com.facebook.dto.notifications.NotificationSqlResult;
import com.facebook.dto.post.Author;
import com.facebook.model.AppUser;
import com.facebook.model.notifications.Notification;
import com.facebook.model.notifications.NotificationType;
import com.facebook.model.posts.Post;
import com.facebook.model.posts.PostStatus;
import com.facebook.model.posts.PostType;
import com.facebook.repository.notifications.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовий клас для перевірки роботи репозиторію повідомлень.
 */
@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotificationRepository notificationRepository;

    private AppUser user;

    private Post post;

    private Notification notification;

    /**
     * Підготовка даних перед кожним тестом.
     */
    @BeforeEach
    void setUp() {

        // Створення та збереження користувача
        user = new AppUser();
        user.setName("Іван");
        user.setSurname("Іваненко");
        user.setUsername("ivan123");
        user.setEmail("ivan@example.com");
        user.setPassword("Password0!");
        user.setRoles(new String[]{"USER"});
        entityManager.persist(user);

        // Створення та збереження ініціатора новини
        AppUser initiator = new AppUser();
        initiator.setName("Olga");
        initiator.setSurname("Gorkavchenko");
        initiator.setUsername("OlgaGorka");
        initiator.setEmail("OlgaGorka@example.com");
        initiator.setPassword("Password1!");
        initiator.setRoles(new String[]{"USER"});
        entityManager.persist(initiator);

        // Створення та збереження поста
        post = new Post();
        post.setStatus(PostStatus.DRAFT);
        post.setUser(user);
        post.setType(PostType.POST);
        entityManager.persist(post);

        // Створення повідомлення
        notification = new Notification();
        notification.setUser(user);
        notification.setInitiator(initiator);
        notification.setPost(post);
        notification.setMessage("Message test");
        notification.setType(NotificationType.POST_COMMENTED);
        entityManager.persist(notification);
    }

    /**
     * Тест перевіряє вибірку повідомлень за ідентифікатором користувача.
     */
    @Test
    void testFindByUserId() {
        Page<NotificationSqlResult> notifications = notificationRepository
                .findByUserId(user.getId(),
                        PageRequest.of(0, 10));
        assertThat(notifications).hasSize(1);
    }

    /**
     * Тест перевіряє видалення повідомлень за ідентифікатором поста.
     */
    @Test
    void testDeleteByPostId() {
        notificationRepository.deleteByPostId(post.getId());
        Notification foundNotification = entityManager
                .find(Notification.class,
                        notification.getId());
        assertThat(foundNotification).isNull();
    }

    /**
     * Тест перевіряє підрахунок кількості непрочитаних повідомлень користувача.
     */
    @Test
    void testCountByUserIdAndIsRead() {
        Notification unreadNotification = new Notification();
        unreadNotification.setUser(user);
        unreadNotification.setRead(false);
        entityManager.persist(unreadNotification);

        Long unreadCount = notificationRepository
                .countByUserIdAndIsRead(user.getId(),
                        false);
        assertThat(unreadCount).isEqualTo(2L);
    }

    /**
     * Перевіряє рівність даних повідомлення та результату SQL-запиту.
     *
     * @param expected очікуваний об'єкт {@link Notification}.
     * @param actual реальний результат {@link NotificationSqlResult}.
     */
    private void assertNotificationEquals(Notification expected, NotificationSqlResult actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUser().getId(), actual.getUserId());
        assertEquals(expected.getMessage(), actual.getMessage());
        assertEquals(expected.isRead(), actual.isRead());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getPost().getId(), actual.getPostId());
        assertEquals(
                expected.getCreatedDate().truncatedTo(ChronoUnit.MILLIS),
                actual.getCreatedDate().truncatedTo(ChronoUnit.MILLIS)
        );
        assertEquals(
                expected.getLastModifiedDate().truncatedTo(ChronoUnit.MILLIS),
                actual.getLastModifiedDate().truncatedTo(ChronoUnit.MILLIS)
        );

        AppUser initiatorExpected = expected.getInitiator();
        Author initiatorActual = actual.getInitiator();
        assertNotNull(initiatorActual);
        assertEquals(initiatorExpected.getId(), initiatorActual.getUserId());
        assertEquals(initiatorExpected.getName(), initiatorActual.getName());
        assertEquals(initiatorExpected.getSurname(), initiatorActual.getSurname());
        assertEquals(initiatorExpected.getUsername(), initiatorActual.getUsername());
    }

    /**
     * Тестує отримання даних повідомлення за його ідентифікатором.
     */
    @Test
    void testFindByNotificationId() {
        Optional<NotificationSqlResult> result = notificationRepository
                .findByNotificationId(notification.getId());

        assertTrue(result.isPresent());
        NotificationSqlResult notificationResult = result.get();

        assertNotificationEquals(notification, notificationResult);
    }

    /**
     * Тестує отримання даних повідомлень за ідентифікатором користувача.
     */
    @Test
    void testFindByUserIdWithInitiatorDetails() {
        Page<NotificationSqlResult> results = notificationRepository
                .findByUserId(user.getId(), PageRequest.of(0, 10));

        assertThat(results).hasSize(1);
        NotificationSqlResult notificationResult = results.getContent().get(0);

        assertNotificationEquals(notification, notificationResult);
    }

    /**
     * Тест перевіряє видалення повідомлень за ідентифікаторами ініціатора, користувача та типу повідомлення.
     */
    @Test
    void testDeleteByInitiatorAndUserAndType() {
        // Перед видаленням
        long countBefore = notificationRepository.count();

        Notification existingNotification = entityManager.find(Notification.class, notification.getId());
        assertNotNull(existingNotification);

        // Видалення повідомлення
        notificationRepository
                .deleteByInitiatorAndUserAndType(notification.getInitiator().getId(),
                        user.getId(),
                        NotificationType.POST_COMMENTED);

        entityManager.flush();
        entityManager.clear();

        // Перевірка видалення
        long countAfter = notificationRepository.count();
        assertThat(countBefore).isGreaterThan(countAfter);

        // Перевірка, що повідомлення більше не існує в базі даних
        Notification foundNotification = entityManager.find(Notification.class, notification.getId());
        assertThat(foundNotification).isNull();
    }

}


