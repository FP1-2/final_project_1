package com.facebook.controller;

import com.facebook.TestConfig;
import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.appuser.AppUserEditRequest;
import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.appuser.UserNewPasswordRequest;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import com.facebook.service.CurrentUserService;
import com.facebook.service.ResetPasswordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AppUserController.class)
@AutoConfigureMockMvc
@WithMockUser
@Import(TestConfig.class)
class AppUserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ResetPasswordService resetPasswordService;
    @MockBean
    private  AppUserService appUserService;
    @MockBean
    private  CurrentUserService currentUserService;
    @MockBean
    private  AppUserFacade appUserFacade;

    @Autowired
    private ObjectMapper mapper;
    private final String EMAIL = "test@example.com";
    private final String TOKEN = "validToken";
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AppUserController(resetPasswordService, appUserService,currentUserService,appUserFacade)).build();
    }
    @Test
    void resetPasswordTest() throws Exception {

        doNothing().when(resetPasswordService).sendResetPasswordLink(any(String.class));

        mockMvc.perform(post("/api/users/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMAIL))
                .andExpect(status().isOk());
    }
    @Test
    void updatePasswordTest() throws Exception {
        UserNewPasswordRequest user = new UserNewPasswordRequest();
        user.setEmail(EMAIL);
        user.setNewPassword("newPass123!");
        doNothing().when(resetPasswordService).resetUserPassword(any(String.class), eq(user));

        mockMvc.perform(put("/api/users/update-password/{token}", TOKEN)
                        .with(csrf())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful"));
    }
    @Test
    void updatePasswordTestWithInvalidPassword() throws Exception {
        UserNewPasswordRequest user = new UserNewPasswordRequest();
        user.setEmail(EMAIL);
        user.setNewPassword("12345");
        doNothing().when(resetPasswordService).resetUserPassword(any(String.class), eq(user));

        mockMvc.perform(put("/api/users/update-password/{token}", TOKEN)
                        .with(csrf())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testEditUserInfo()  throws Exception {
        Long userId = 1L;
        when(currentUserService.getCurrentUserId()).thenReturn(userId);

        AppUserEditRequest userEditRequest = new AppUserEditRequest();
        AppUser editedUser = new AppUser();
        AppUserResponse expectedResponse = new AppUserResponse();
        expectedResponse.setId(userId);
        expectedResponse.setName("testName");
        expectedResponse.setUsername("testUsername");

        when(appUserService.editUser(userId, userEditRequest)).thenReturn(Optional.of(editedUser));
        when(appUserFacade.convertToAppUserResponse(editedUser)).thenReturn(expectedResponse);

        mockMvc.perform(put("/api/users/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userEditRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("testName"))
                .andExpect(jsonPath("$.username").value("testUsername"));

    }
    @Test
    void testSearchUserByKeyword() throws Exception {
        List<AppUserChatResponse> users = new ArrayList<>();
        users.add(new AppUserChatResponse());
        when(appUserService.findUserByKeyword(anyString(), any(Pageable.class))).thenReturn(users);

        mockMvc.perform(get("/api/users/search")
                        .param("input", "test")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isMap());
    }
    @Test
    void testGetUserAppById() throws Exception{
        Long userId = 900L;
        AppUserResponse appUserResponse = new AppUserResponse();
        appUserResponse.setId(userId);
        when(appUserService.findById(userId)).thenReturn(Optional.of(new AppUser()));
        when(appUserFacade.convertToAppUserResponse(any(AppUser.class))).thenReturn(appUserResponse);

        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(900));
    }
}
