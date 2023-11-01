package com.facebook.controller;

import com.facebook.TestConfig;
import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.dto.message.MessageResponse;
import com.facebook.dto.message.MessageResponseList;
import com.facebook.facade.ChatFacade;
import com.facebook.facade.MessageFacade;
import com.facebook.model.chat.ContentType;
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


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(classes = ChatController.class)
@Import(TestConfig.class)
@AutoConfigureMockMvc
@WithMockUser(username="testUser", password="test")
public class ChatControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChatFacade chatFacade;
    @MockBean
    private MessageFacade messageFacade;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ChatController(messageFacade, chatFacade)).build();
    }
    @Test
    void testGetAllChats() throws Exception {
        List<ChatResponseList> mockChats = new ArrayList<>();
        mockChats.add(new ChatResponseList(){{setId(12L);
                                                setChatParticipant(new AppUserChatResponse());
                                                setLastMessage(new MessageResponseList());}});
        when(chatFacade.getAllUserChats(any(Pageable.class))).thenReturn(mockChats);


        mockMvc.perform(get("/api/chats")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(12))
                .andExpect(jsonPath("$[0].chatParticipant").exists())
                .andExpect(jsonPath("$[0].lastMessage").exists());
    }

    @Test
    void testGetChatById() throws Exception {
        Long chatId = 15L;
        ChatResponse mockChat = new ChatResponse(){{setId(chatId);}};
        when(chatFacade.getChatById(anyLong())).thenReturn(mockChat);

        mockMvc.perform(get("/api/chats/{id}", chatId)
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(15));
    }
    @Test
    void testCreateChat() throws Exception {
        Long chatId = 15L;
        String username = "testUsername";
        ChatResponse mockChat = new ChatResponse(){{setId(chatId);
                                                    setChatParticipant(new AppUserChatResponse(){
                                                        {setUsername(username);}});}};
        when(chatFacade.createChat(anyString())).thenReturn(mockChat);
        mockMvc.perform(post("/api/chats/{username}", username)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(15))
                .andExpect(jsonPath("$.chatParticipant").isMap())
                .andExpect(jsonPath("$.chatParticipant.username").value(username));
    }
    @Test
    void testGetAllMessages() throws Exception {
        List<MessageResponse> mockMessages = new ArrayList<>();
        Long messageId = 20L;
        Long chatId = 5L;
        mockMessages.add(new MessageResponse(){{setId(messageId);
                                                setContentType(ContentType.IMAGE);
                                                setContent("someText");
                                                setChat(new ChatResponse(){{setId(chatId);}});}});
        when(messageFacade.getAllMessages(anyLong(), any(Pageable.class))).thenReturn(mockMessages);

        mockMvc.perform(get("/api/chats/messages/{chatId}", chatId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(20))
                .andExpect(jsonPath("$[0].contentType").isString())
                .andExpect(jsonPath("$[0].content").isString())
                .andExpect(jsonPath("$[0].chat").isMap())
                .andExpect(jsonPath("$[0].chat.id").value(5));

    }
    @Test
    void testGetACountOfUnreadMessage() throws Exception {
        when(messageFacade.countUnreadMessage()).thenReturn(5L);

        mockMvc.perform(get("/api/chats/messages/unread")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(5));
    }
    @Test
    public void testSearchChats() throws Exception {
        List<ChatResponse> mockChats = new ArrayList<>();
        mockChats.add(new ChatResponse());
        when(chatFacade.searchChats(anyString(), any(Pageable.class))).thenReturn(mockChats);

        mockMvc.perform(get("/api/chats/search")
                    .param("input", "test")
                    .param("page", "0")
                    .param("size", "10")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isMap());
    }
}
