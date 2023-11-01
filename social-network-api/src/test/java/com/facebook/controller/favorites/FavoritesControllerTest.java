package com.facebook.controller.favorites;

import com.facebook.controller.FavoritesController;
import com.facebook.dto.post.Author;
import com.facebook.dto.post.PostResponse;
import com.facebook.model.posts.PostStatus;
import com.facebook.model.posts.PostType;
import com.facebook.service.CurrentUserService;
import com.facebook.service.EmailHandlerService;
import com.facebook.service.favorites.FavoritesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тестовий клас для {@link FavoritesController}.
 * @see FavoritesController
 * @see Author
 * @see PostResponse
 */
@SpringBootTest
@AutoConfigureMockMvc
class FavoritesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailHandlerService emailHandlerService;

    @MockBean
    private CurrentUserService currentUserService;

    @MockBean
    private FavoritesService favoritesService;

    /**
     * Тест для перевірки додавання поста до улюбленого.
     */
    @Test
    @WithMockUser
    void addToFavoritesTest() throws Exception {
        Long postId = 1L;
        Long userId = 1L;

        when(currentUserService.getCurrentUserId()).thenReturn(userId);

        mockMvc.perform(post("/api/favorites/" + postId))
                .andExpect(status().isCreated());

        verify(favoritesService, times(1))
                .addToFavorites(postId, userId);
    }

    /**
     * Тест для перевірки видалення поста з улюбленого.
     */
    @Test
    @WithMockUser
    void removeFromFavoritesTest() throws Exception {
        Long postId = 1L;
        Long userId = 1L;

        when(currentUserService.getCurrentUserId()).thenReturn(userId);

        mockMvc.perform(delete("/api/favorites/" + postId))
                .andExpect(status().isNoContent());

        verify(favoritesService, times(1))
                .removeFromFavorites(postId, userId);
    }

    /**
     * Тест для перевірки, чи є пост у "обраному" користувача.
     */
    @Test
    @WithMockUser
    void isPostInFavoritesTest() throws Exception {
        Long postId = 1L;
        Long userId = 1L;

        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(favoritesService.isPostInFavorites(postId, userId)).thenReturn(true);

        mockMvc.perform(get("/api/favorites/" + postId + "/exists"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    /**
     * Тест для перевірки отримання списку улюблених постів користувача.
     */
    @Test
    @WithMockUser
    void getFavoritePostsTest() throws Exception {
        Long userId = 1L;
        Author author = new Author();
        author.setUserId(userId);
        author.setName("Test User");
        author.setSurname("Test Surname");
        author.setUsername("testuser");
        author.setAvatar("https://test.com/avatar.jpg");
        PostResponse mockResponse = new PostResponse();

        mockResponse.setPostId(14L);
        mockResponse.setAuthor(author);
        mockResponse.setCreated_date(LocalDateTime.now());
        mockResponse.setLast_modified_date(LocalDateTime.now());
        mockResponse.setImageUrl("https://test.com/image.jpg");
        mockResponse.setTitle("Test Title");
        mockResponse.setBody("Test Body");
        mockResponse.setStatus(PostStatus.PUBLISHED.name());
        mockResponse.setType(PostType.POST);

        List<PostResponse> content = List.of(mockResponse);
        Page<PostResponse> mockPage = new PageImpl<>(content);

        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(favoritesService
                .findFavoritePostDetailsByUserId(userId, 0, 10, "id,asc"))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/favorites")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].postId").value(14L))
                .andExpect(jsonPath("$.content[0].author.userId").value(1L))
                .andExpect(jsonPath("$.content[0].author.name").value("Test User"))
                .andExpect(jsonPath("$.content[0].author.surname").value("Test Surname"))
                .andExpect(jsonPath("$.content[0].author.username").value("testuser"))
                .andExpect(jsonPath("$.content[0].author.avatar").value("https://test.com/avatar.jpg"))
                .andExpect(jsonPath("$.content[0].created_date").exists())
                .andExpect(jsonPath("$.content[0].last_modified_date").exists())
                .andExpect(jsonPath("$.content[0].imageUrl").value("https://test.com/image.jpg"))
                .andExpect(jsonPath("$.content[0].title").value("Test Title"))
                .andExpect(jsonPath("$.content[0].body").value("Test Body"))
                .andExpect(jsonPath("$.content[0].status").value("PUBLISHED"))
                .andExpect(jsonPath("$.content[0].type").value("POST"));
    }

}

