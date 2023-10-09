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
 * {@link Like} за допомогою JPA.
 *
 * <p>Використовує анотацію {@code @DataJpaTest}
 * для інтеграційного тестування з базою даних в пам'яті.
 * Перевіряє коректність роботи з датами та обмеженнями полів.</p>
 *
 * @see Like
 * @see DataJpaTest
 */
@DataJpaTest
public class LikeTest {

    @Autowired
    private TestEntityManager tem;

    private AppUser createAndSaveTestUser() {
        AppUser user = new AppUser();
        user.setName("Test");
        user.setSurname("User");
        user.setUsername("test.user");
        user.setEmail("likeTest.user@email.com");
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
     * Тестує обмеження: користувач, який поставив лайк
     * не повинен бути null.
     * Очікується ConstraintViolationException
     * при спробі збереження.
     */
    @Test
    void userNotNull() {
        AppUser user = createAndSaveTestUser();
        Like like = new Like();
        like.setPost(createAndSaveTestPost(user));

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(like));
    }

    /**
     * Тестує обмеження: пост, який було лайкнуто,
     * не повинен бути null.
     * Очікується ConstraintViolationException
     * при спробі збереження.
     */
    @Test
    void postNotNull() {
        AppUser user = createAndSaveTestUser();
        Like like = new Like();
        like.setUser(user);

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(like));
    }

    /**
     * Тестує правильність встановлення дати
     * при створенні лайка.
     * Переконується, що поле createdDate встановлене та не є null.
     */
    @Test
    void createdDateNotNull() {
        AppUser user = createAndSaveTestUser();
        Like like = new Like();
        like.setUser(user);
        like.setPost(createAndSaveTestPost(user));

        Like savedLike = tem.persistAndFlush(like);

        assertThat(savedLike.getCreatedDate()).isNotNull();
    }

    /**
     * Тестує обмеження: комбінація користувача і посту
     * повинна бути унікальною для лайка.
     * Очікується ConstraintViolationException
     * при спробі збереження.
     */
    @Test
    void likeShouldBeUnique() {
        AppUser user = createAndSaveTestUser();
        Post post = createAndSaveTestPost(user);

        Like like1 = new Like();
        like1.setUser(user);
        like1.setPost(post);
        tem.persistAndFlush(like1);

        Like like2 = new Like();
        like2.setUser(user);
        like2.setPost(post);

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(like2));
    }

}

