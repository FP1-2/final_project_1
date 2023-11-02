package com.facebook.controller.notifications;

import com.facebook.dto.notifications.NotificationResponse;
import com.facebook.dto.post.Author;
import com.facebook.exception.UnauthorizedException;
import com.facebook.service.CurrentUserService;
import com.facebook.service.EmailHandlerService;
import com.facebook.service.notifications.NotificationService;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тестовий клас для перевірки роботи контролера сповіщень.
 * <p>
 * Перелік тестів:
 * <ul>
 *     <li>{@link #getNotificationsTest()}</li>
 *     <li>{@link #markNotificationAsReadTest()}</li>
 *     <li>{@link #getUnreadNotificationCountTest()}</li>
 * </ul>
 * </p>
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailHandlerService emailHandlerService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private CurrentUserService currentUserService;

    /**
     * Тест для перевірки отримання списку сповіщень користувача.
     * Перевіряється коректність даних у відповіді та їх відповідність очікуваним.
     */
    @Test
    @WithMockUser
    void getNotificationsTest() throws Exception {
        Long userId = 1L;

        NotificationResponse mockResponse = new NotificationResponse();
        mockResponse.setId(14L);
        mockResponse.setUserId(userId);

        Author initiator = new Author();
        initiator.setUserId(2L);

        mockResponse.setInitiator(initiator);
        mockResponse.setPostId(3L);
        mockResponse.setMessage("Clementina DuBuque Liked my post");
        mockResponse.setRead(false);
        mockResponse.setType("POST_LIKED");
        mockResponse.setCreatedDate(LocalDateTime.now());
        mockResponse.setLastModifiedDate(LocalDateTime.now());

        List<NotificationResponse> content = List.of(mockResponse);
        Page<NotificationResponse> mockPage = new PageImpl<>(content);

        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(notificationService.getNotificationsByUserId(userId, 0, 10)).thenReturn(mockPage);

        mockMvc.perform(get("/api/notifications")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(14L))
                .andExpect(jsonPath("$.content[0].userId").value(1L))
                .andExpect(jsonPath("$.content[0].initiator.userId").value(2L))
                .andExpect(jsonPath("$.content[0].postId").value(3L))
                .andExpect(jsonPath("$.content[0].message").value("Clementina DuBuque Liked my post"))
                .andExpect(jsonPath("$.content[0].type").value("POST_LIKED"))
                .andExpect(jsonPath("$.content[0].createdDate").exists())
                .andExpect(jsonPath("$.content[0].lastModifiedDate").exists())
                .andExpect(jsonPath("$.content[0].read").value(false))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.sort.empty").value(true))
                .andExpect(jsonPath("$.sort.sorted").value(false))
                .andExpect(jsonPath("$.sort.unsorted").value(true))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.empty").value(false));
    }

    /**
     * Тестує отримання повідомлення за його ідентифікатором.
     * Перевіряє, що метод getNotification контролера коректно повертає.
     * об'єкт NotificationResponse для заданого ідентифікатора повідомлення.
     * @throws Exception якщо виникає помилка під час виконання запиту.
     */
    @Test
    @WithMockUser
    void getNotificationTest() throws Exception {
        Long notificationId = 14L;
        Long userId = 1L;

        NotificationResponse mockResponse = new NotificationResponse();
        mockResponse.setId(notificationId);
        mockResponse.setUserId(userId);
        // ... ініціалізація інших полів mockResponse не додано,
        // бо мапінг той самий що і для Page

        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(notificationService.getNotificationById(notificationId, userId)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/notifications/{id}", notificationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notificationId))
                .andExpect(jsonPath("$.userId").value(userId))
                // ...
                .andExpect(jsonPath("$.message").value(mockResponse.getMessage()));
    }


    /**
     * Тест перевіряє, що метод markNotificationAsRead сервісу викликається з
     * правильними параметрами та повертає статус 200 OK при спробі
     * позначити повідомлення як прочитане.
     */
    @Test
    @WithMockUser(username="user1", roles={"USER"})
    void markNotificationAsReadTest() throws Exception {
        Long notificationId = 1L;
        Long userId = 2L;

        when(currentUserService.getCurrentUserId()).thenReturn(userId);

        mockMvc.perform(post("/api/notifications/" + notificationId + "/mark-as-read"))
                .andExpect(status().isOk());

        verify(notificationService, times(1))
                .markNotificationAsRead(notificationId, userId);
    }

    /**
     * Тест перевіряє, що система правильно обробляє ситуацію, коли користувач без належних прав
     * намагається позначити повідомлення як прочитане. Очікується, що в такому випадку
     * буде кинуто UnauthorizedException і повернуто статус відповіді 401 Unauthorized.
     *
     * @throws UnauthorizedException якщо виникає помилка під час виконання запиту
     */
    @Test
    @WithMockUser(username="user1", roles={"USER"})
    void markNotificationAsReadUnauthorizedTest() throws Exception {
        Long notificationId = 1L;
        Long userId = 2L;

        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        doThrow(new UnauthorizedException("User does not have access to mark this notification as read"))
                .when(notificationService).markNotificationAsRead(notificationId, userId);

        mockMvc.perform(post("/api/notifications/" + notificationId + "/mark-as-read"))
                .andExpect(status().isUnauthorized());

        verify(notificationService, times(1))
                .markNotificationAsRead(notificationId, userId);
    }

    /**
     * Тест для перевірки отримання кількості непрочитаних сповіщень користувача.
     */
    @Test
    @WithMockUser
    void getUnreadNotificationCountTest() throws Exception {
        Long userId = 1L;
        Long unreadCount = 5L;

        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(notificationService.getUnreadNotificationCount(userId)).thenReturn(unreadCount);

        mockMvc.perform(get("/api/notifications/unread-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

}


