package com.facebook.model.posts;


import com.facebook.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тестовий клас для перевірки функціональності
 * {@link Post} за допомогою JPA.
 *
 * <p>Використовує анотацію {@code @DataJpaTest}
 * для інтеграційного тестування з базою даних в пам'яті.
 * Перевіряє коректність роботи з датами,
 * обмеженнями полів та статусами поста.</p>
 *
 * @see Post
 * @see DataJpaTest
 */
@DataJpaTest
class PostTest {

    static final String TITLE = "Title";

    static final String BODY = "Body";

    static final String IMAGE = "image.jpg";

    @Autowired
    private TestEntityManager tem;

    /**
     * Створює тестового користувача
     * та зберігає його в базі даних.
     */
    private AppUser createAndSaveTestUser() {
        AppUser user = new AppUser();
        user.setName("Test");
        user.setSurname("User");
        user.setUsername("test.user");
        user.setEmail("test.user@email.com");
        user.setPassword("password");
        return tem.persistAndFlush(user);
    }

    /**
     * Тестує правильність встановлення дати
     * при створенні посту.
     * Переконується, що поля createdDate та
     * lastModifiedDate встановлені та не є null.
     */
    @Test
    void dateSetting() {
        Post post = createPost(createAndSaveTestUser());

        Post savedPost = tem.persistAndFlush(post);

        assertThat(savedPost.getCreatedDate())
                .isNotNull();
        assertThat(savedPost.getLastModifiedDate())
                .isNotNull();
    }

    /**
     * Тестує правильність оновлення дати модифікації поста.
     * При зміні даних поста дата модифікації повинна оновлюватися.
     * <ol>
     *     <li>Створення нового поста та його збереження.</li>
     *     <li>Отримання дати модифікації.</li>
     *     <li>Зміна даних поста та повторне його збереження.</li>
     *     <li>Перевірка, що дата модифікації була оновлена.</li>
     * </ol>
     */
    @Test
    void dateModification() throws InterruptedException {
        // 1. Створення і збереження поста
        Post post = createPost(createAndSaveTestUser());
        Post savedPost = tem.persistAndFlush(post);

        // 2. Отримання дати модифікації
        LocalDateTime initialLastModifiedDate = savedPost.getLastModifiedDate();

        // Додаємо затримку
        Thread.sleep(50);

        // 3. Зміна даних і повторне збереження
        savedPost.setTitle("Updated Title");
        tem.persistAndFlush(savedPost);

        // Оновлюємо стан об'єкта savedPost з бази даних
        tem.refresh(savedPost);

        // 4. Перевірка, що дата модифікації змінилася
        assertThat(savedPost.getLastModifiedDate()).isNotEqualTo(initialLastModifiedDate);
        assertThat(savedPost.getLastModifiedDate()).isAfterOrEqualTo(initialLastModifiedDate);
    }


    /**
     * Створює та повертає новий об'єкт типу "Post".
     *
     * @param user користувач, який створює повідомлення
     * @return новий об'єкт типу "Post"
     */
    private Post createPost(AppUser user){
        Post post = new Post();
        post.setTitle(TITLE);
        post.setBody(BODY);
        post.setUser(user);
        post.setImageUrl(IMAGE);
        post.setType(PostType.POST);
        return post;
    }

}


