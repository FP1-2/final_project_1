package com.facebook.controller;

import com.facebook.TestConfig;
import com.facebook.exception.InvalidTokenException;
import com.facebook.service.AppUserService;
import com.facebook.service.ResetPasswordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private AppUserService userService;
    @Autowired
    private ObjectMapper mapper;
    private final String EMAIL = "test@example.com";
    private final String TOKEN = "validToken";
    @Test
    void resetPasswordTest() throws Exception {

        doNothing().when(resetPasswordService).sendResetPasswordLink(any(String.class), any(String.class));

        mockMvc.perform(post("/api/users/reset-password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EMAIL))
                .andExpect(status().isOk());
    }
    @Test
    void checkResetTokenValidTest() throws Exception {
        when(resetPasswordService.isResetTokenValid(any(String.class), any(String.class))).thenReturn(true);

        mockMvc.perform(get("/api/users/reset-password/{token}", TOKEN)
                        .param("em", EMAIL))
                .andExpect(status().isOk());
    }
    @Test
    void checkResetTokenInvalidTest() throws Exception {
        when(resetPasswordService.isResetTokenValid(any(String.class), any(String.class))).thenReturn(false);

        mockMvc.perform(get("/api/users/reset-password/{token}", TOKEN)
                        .param("em", EMAIL))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InvalidTokenException));
    }
//    @Test
//    void updatePasswordTest() throws Exception {
//        UserNewPasswordRequest user = new UserNewPasswordRequest(EMAIL, "Password123!");
//        doNothing().when(resetPasswordService).resetUserPassword(any(String.class), any(UserNewPasswordRequest.class));
//
//        mockMvc.perform(put("/api/users/update-password/{token}", TOKEN)
//                        .with(csrf())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(mapper.writeValueAsString(user)))
//                        .andDo(print())
//                .andExpect(status().isOk());
//    }

}
