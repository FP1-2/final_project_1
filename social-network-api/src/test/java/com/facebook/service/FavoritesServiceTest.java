package com.facebook.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.facebook.dto.post.PostResponse;
import com.facebook.facade.PostFacade;
import com.facebook.model.favorites.Favorite;
import com.facebook.model.posts.Post;
import com.facebook.repository.favorites.FavoriteRepository;
import com.facebook.repository.posts.PostRepository;
import com.facebook.service.favorites.FavoritesService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Клас для тестування сервісу обраних постів.
 *
 * <p>Список тестів:
 * <ul>
 *     <li>{@link #addToFavoritesTest()}</li>
 *     <li>{@link #removeFromFavoritesTest()}</li>
 *     <li>{@link #isPostInFavoritesTest()}</li>
 *     <li>{@link #findFavoritePostDetailsByUserIdTest()}</li>
 * </ul>
 */
@SpringBootTest
class FavoritesServiceTest {

    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private PostFacade postFacade;

    @MockBean
    private EmailHandlerService emailHandlerService;

    @MockBean
    private FavoriteRepository favoriteRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private EntityManager em;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Тест для перевірки додавання поста до обраних.
     */
    @Test
    void addToFavoritesTest() {
        Long postId = 1L;
        Long userId = 1L;

        when(favoriteRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.empty());
        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));

        favoritesService.addToFavorites(postId, userId);

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    /**
     * Тест для перевірки видалення поста з обраних.
     */
    @Test
    void removeFromFavoritesTest() {
        Long postId = 1L;
        Long userId = 1L;

        Favorite favorite = new Favorite();
        when(favoriteRepository.findByUserIdAndPostId(userId, postId)).thenReturn(Optional.of(favorite));

        favoritesService.removeFromFavorites(postId, userId);

        verify(favoriteRepository, times(1)).delete(favorite);
    }

    /**
     * Тест для перевірки наявності поста в обраних користувача.
     */
    @Test
    void isPostInFavoritesTest() {
        Long postId = 1L;
        Long userId = 1L;

        when(favoriteRepository.findByUserIdAndPostId(userId, postId))
                .thenReturn(Optional.of(new Favorite()));

        boolean result = favoritesService.isPostInFavorites(postId, userId);

        assertTrue(result);
    }

    /**
     * Тест для перевірки отримання деталей обраних постів користувачем.
     * Примітка: Преобразування даних відбувається в PostFacade. Якщо виникають проблеми,
     * рекомендується перевірити логіку в цьому фасаді.
     *
     * @see PostFacade
     */
    @Test
    void findFavoritePostDetailsByUserIdTest() {
        Long userId = 1L;
        int page = 0;
        int size = 10;
        String sort = "id,asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("NAME", "Ervin");
        mockResult.put("STATUS", "PUBLISHED");
        mockResult.put("IMAGE_URL", "https://source.unsplash.com/random?wallpapers");
        mockResult.put("COMMENT_IDS", "72,73");
        mockResult.put("SURNAME", "Smith");
        mockResult.put("USER_ID", 3L);
        mockResult.put("USERNAME", "ErvinNickname");
        mockResult.put("BODY", "Facere sit possimus ut corporis nesciunt.");
        mockResult.put("POST_ID", 58L);
        mockResult.put("TITLE", "sed");
        mockResult.put("CREATED_DATE", LocalDateTime.parse("2023-10-31T21:33:36.192"));
        mockResult.put("LAST_MODIFIED_DATE", LocalDateTime.parse("2023-10-31T21:33:36.192"));
        mockResult.put("AVATAR", "https://via.placeholder.com/150/66b7d2");

        List<Map<String, Object>> mockResults = List.of(mockResult);

        when(favoriteRepository.findFavoritePostDetailsByUserId(userId, pageable)).thenReturn(mockResults);
        when(favoriteRepository.countFavoritesByUserId(userId)).thenReturn(10L);

        Page<PostResponse> result = favoritesService.findFavoritePostDetailsByUserId(userId, page, size, sort);

        assertEquals(10, result.getTotalElements());

        PostResponse postResponse = result.getContent().get(0);
        assertEquals("Ervin", postResponse.getAuthor().getName());
        assertEquals("Smith", postResponse.getAuthor().getSurname());
        assertEquals("ErvinNickname", postResponse.getAuthor().getUsername());
        assertEquals("https://via.placeholder.com/150/66b7d2", postResponse.getAuthor().getAvatar());
        assertEquals("Facere sit possimus ut corporis nesciunt.", postResponse.getBody());
        assertEquals("sed", postResponse.getTitle());
        assertEquals(58L, postResponse.getPostId());
    }

}

