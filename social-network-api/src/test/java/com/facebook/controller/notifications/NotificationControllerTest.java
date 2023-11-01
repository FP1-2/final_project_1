package com.facebook.controller.notifications;

import com.facebook.dto.notifications.NotificationResponse;
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
        mockResponse.setInitiatorId(2L);
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
                .andExpect(jsonPath("$.content[0].initiatorId").value(2L))
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
     * Тест для перевірки позначення сповіщення як прочитаного.
     * Перевіряється статус відповіді та виклик відповідного методу сервісу.
     */
    @Test
    @WithMockUser
    void markNotificationAsReadTest() throws Exception {
        Long notificationId = 1L;

        mockMvc.perform(post("/api/notifications/" + notificationId + "/mark-as-read"))
                .andExpect(status().isOk());

        verify(notificationService, times(1))
                .markNotificationAsRead(notificationId);
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


