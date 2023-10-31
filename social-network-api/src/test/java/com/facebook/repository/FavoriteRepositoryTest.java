package com.facebook.repository;

import com.facebook.model.AppUser;
import com.facebook.model.favorites.Favorite;
import com.facebook.model.posts.Post;
import com.facebook.model.posts.PostStatus;
import com.facebook.model.posts.PostType;
import com.facebook.repository.favorites.FavoriteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовий клас для {@link FavoriteRepository}.
 * Використовує in-memory базу даних H2 для тестування CRUD операцій.
 */
@DataJpaTest
class FavoriteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private AppUser user;

    private Post post;

    @BeforeEach
    void setUp() {
        user = new AppUser();
        user.setName("Ім'я");
        user.setSurname("Прізвище");
        user.setUsername("username");
        user.setEmail("email@example.com");
        user.setPassword("password");
        entityManager.persist(user);

        post = new Post();
        post.setTitle("Заголовок");
        post.setBody("Тіло поста");
        post.setStatus(PostStatus.PUBLISHED);
        post.setType(PostType.POST);
        post.setUser(user);
        entityManager.persist(post);
    }

    /**
     * Тестує знаходження об'єкта {@link Favorite} за userId та postId.
     */
    @Test
    void testFindByUserIdAndPostId() {
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setPost(post);
        entityManager.persist(favorite);

        Optional<Favorite> foundFavorite = favoriteRepository
                .findByUserIdAndPostId(user.getId(), post.getId());

        assertTrue(foundFavorite.isPresent());
        assertEquals(favorite.getId(), foundFavorite.get().getId());
    }

    /**
     * Тестує відсутність об'єкта {@link Favorite} за заданими userId та postId.
     */
    @Test
    void testFavoriteNotExists() {
        Optional<Favorite> foundFavorite = favoriteRepository
                .findByUserIdAndPostId(user.getId(), post.getId() + 1);
        assertFalse(foundFavorite.isPresent());
    }

    /**
     * Тестує підрахунок кількості об'єктів {@link Favorite} за userId.
     */
    @Test
    void testCountFavoritesByUserId() {
        Favorite favorite1 = new Favorite();
        favorite1.setUser(user);
        favorite1.setPost(post);
        entityManager.persist(favorite1);

        Favorite favorite2 = new Favorite();
        favorite2.setUser(user);
        favorite2.setPost(post);
        entityManager.persist(favorite2);

        Long count = favoriteRepository.countFavoritesByUserId(user.getId());
        assertEquals(2, count);
    }

}


