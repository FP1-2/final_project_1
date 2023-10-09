package com.facebook.model.posts;

import com.facebook.model.AppUser;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестовий клас для перевірки функціональності
 * {@link Comment} за допомогою JPA.
 *
 * <p>Використовує анотацію {@code @DataJpaTest}
 * для інтеграційного тестування з базою даних в пам'яті.
 * Перевіряє коректність роботи з датами та обмеженнями полів.</p>
 *
 * @see Comment
 * @see DataJpaTest
 */
@DataJpaTest
public class CommentTest {

    @Autowired
    private TestEntityManager tem;

    private AppUser createAndSaveTestUser() {
        AppUser user = new AppUser();
        user.setName("Test");
        user.setSurname("User");
        user.setUsername("test.user");
        user.setEmail("commentTest.user@email.com");
        user.setPassword("password");
        return tem.persistAndFlush(user);
    }

    private Post createAndSaveTestPost(AppUser user) {
        Post post = new Post();
        post.setTitle("Title");
        post.setBody("Body");
        post.setStatus(PostStatus.DRAFT);
        post.setUser(user);
        post.setImageUrl("image.jpg");
        return tem.persistAndFlush(post);
    }

    /**
     * Тестує обмеження: користувач, який залишив коментар
     * не повинен бути null.
     * Очікується ConstraintViolationException
     * при спробі збереження.
     */
    @Test
    void userNotNull() {
        Comment comment = new Comment();
        comment.setPost(createAndSaveTestPost(createAndSaveTestUser()));
        comment.setContent("Test comment content.");

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(comment));
    }

    /**
     * Тестує обмеження: пост, до якого було залишено коментар,
     * не повинен бути null.
     * Очікується ConstraintViolationException
     * при спробі збереження.
     */
    @Test
    void postNotNull() {
        Comment comment = new Comment();
        comment.setUser(createAndSaveTestUser());
        comment.setContent("Test comment content.");

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(comment));
    }

    /**
     * Тестує обмеження: вміст коментаря не повинен бути null.
     * Очікується ConstraintViolationException
     * при спробі збереження.
     */
    @Test
    void contentNotNull() {
        AppUser user = createAndSaveTestUser();
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(createAndSaveTestPost(user));

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(comment));
    }

    /**
     * Тестує правильність встановлення дати
     * при створенні коментаря.
     * Переконується, що поле createdDate встановлене та не є null.
     */
    @Test
    void createdDateNotNull() {
        AppUser user = createAndSaveTestUser();
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(createAndSaveTestPost(user));
        comment.setContent("Test comment content.");

        Comment savedComment = tem.persistAndFlush(comment);

        assertThat(savedComment.getCreatedDate()).isNotNull();
    }

}

