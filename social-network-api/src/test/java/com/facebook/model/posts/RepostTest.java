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
 * {@link Repost} за допомогою JPA.
 *
 * <p>Використовує анотацію {@code @DataJpaTest}
 * для інтеграційного тестування з базою даних в пам'яті.
 * Перевіряє коректність роботи з датами та обмеженнями полів.</p>
 *
 * @see Repost
 * @see DataJpaTest
 */
@DataJpaTest
class RepostTest {

    @Autowired
    private TestEntityManager tem;

    private AppUser createAndSaveTestUser() {
        AppUser user = new AppUser();
        user.setName("Test");
        user.setSurname("User");
        user.setUsername("test.user");
        user.setEmail("repostTest.user@email.com");
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
     * Тестує обмеження: користувач, який зробив репост
     * не повинен бути null.
     * Очікується ConstraintViolationException
     * при спробі збереження.
     */
    @Test
    void userNotNull() {
        AppUser user = createAndSaveTestUser();
        Repost repost = new Repost();
        repost.setPost(createAndSaveTestPost(user));

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(repost));
    }

    /**
     * Тестує обмеження: пост, який було репостнуто,
     * не повинен бути null.
     * Очікується ConstraintViolationException
     * при спробі збереження.
     */
    @Test
    void postNotNull() {
        AppUser user = createAndSaveTestUser();
        Repost repost = new Repost();
        repost.setUser(user);

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(repost));
    }

    /**
     * Тестує правильність встановлення дати
     * при створенні репоста.
     * Переконується, що поле createdDate встановлене та не є null.
     */
    @Test
    void createdDateNotNull() {
        AppUser user = createAndSaveTestUser();
        Repost repost = new Repost();
        repost.setUser(user);
        repost.setPost(createAndSaveTestPost(user));

        Repost savedRepost = tem.persistAndFlush(repost);

        assertThat(savedRepost.getCreatedDate()).isNotNull();
    }

    /**
     * Тестує обмеження: комбінація користувача і посту
     * повинна бути унікальною для репоста.
     * Очікується DataIntegrityViolationException
     * при спробі збереження.
     */
    @Test
    void repostShouldBeUnique() {
        AppUser user = createAndSaveTestUser();
        Post post = createAndSaveTestPost(user);

        Repost repost1 = new Repost();
        repost1.setUser(user);
        repost1.setPost(post);
        tem.persistAndFlush(repost1);

        Repost repost2 = new Repost();
        repost2.setUser(user);
        repost2.setPost(post);

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(repost2));
    }

}

