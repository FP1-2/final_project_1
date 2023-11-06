package com.facebook.facade;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.dto.chat.ChatSqlResult;
import com.facebook.exception.NotFoundException;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.service.AppUserService;
import com.facebook.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ChatFacade.class)
class ChatFacadeTest {
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private  ChatService chatService;
    @MockBean
    private  AppUserService appUserService;
    @Autowired
    private ChatFacade chatFacade;
    private AppUser authUser;
    private AppUser receiverUser;
    private AppUserChatResponse receiverUserR ;
    private AppUser testUser;
    private AppUserChatResponse testUserR;
    private Chat chat;
    private ChatResponse chatR;

    @BeforeEach
    void setUp() {
        authUser = TestDataFactory.createAuthUser();
        receiverUser = TestDataFactory.createReceiverUser();
        receiverUserR = TestDataFactory.createReceiverUserR();
        testUser = TestDataFactory.createTestUser();
        testUserR = TestDataFactory.createTestUserR();
        chat = TestDataFactory.createChat(authUser, receiverUser);
        chatR = TestDataFactory.createChatResponse(receiverUserR);


    }
    @Test
    void testConvertToChatResponse() {
        when(modelMapper.map(chat, ChatResponse.class)).thenReturn(chatR);
        when(modelMapper.map(receiverUser, AppUserChatResponse.class)).thenReturn(receiverUserR);

        ChatResponse result = chatFacade.convertToChatResponse(chat, receiverUser);

        assertNotNull(result);
        assertEquals(chatR, result);
        assertEquals(chatR.getId(), result.getId());
        assertEquals(chatR.getChatParticipant(), result.getChatParticipant());
        assertEquals("receiverUsername", result.getChatParticipant().getUsername());
    }
    @Test
    void testCreateChat() {

        when(appUserService.getAuthUser()).thenReturn(authUser);
        when(appUserService.findByUsername(anyString())).thenReturn(Optional.of(receiverUser));
        when(chatService.createNewChat(authUser, receiverUser)).thenReturn(chat);
        when(modelMapper.map(chat, ChatResponse.class)).thenReturn(chatR);
        when(modelMapper.map(receiverUser, AppUserChatResponse.class)).thenReturn(receiverUserR);

        ChatResponse result = chatFacade.createChat("receiverUsername");

        assertNotNull(result);
        assertEquals(chatR, result);
        assertEquals(chatR.getId(), result.getId());
        assertEquals(chatR.getChatParticipant(), result.getChatParticipant());
        assertEquals("receiverUsername", result.getChatParticipant().getUsername());
    }
    @Test
    void testGetAllUserChats() {
        when(appUserService.getAuthUser()).thenReturn(authUser);

        List<Chat> chatList = new ArrayList<>();
        chatList.add(chat);
        Chat chat1 = new Chat() {{
            setId(2L);
            setChatParticipants(List.of(authUser, testUser));
        }};
        chatList.add(chat1);

        when(chatService.getAllUserChats(eq(authUser), any(Pageable.class))).thenReturn(new PageImpl<>(chatList));

        ChatResponseList chatResponse = new ChatResponseList();
        chatResponse.setChatParticipant(receiverUserR);
        ChatResponseList chatResponse1 = new ChatResponseList();
        chatResponse1.setChatParticipant(testUserR);

        when(modelMapper.map(chat, ChatResponseList.class)).thenReturn(chatResponse);
        when(modelMapper.map(chat1, ChatResponseList.class)).thenReturn(chatResponse1);
        when(modelMapper.map(receiverUser, AppUserChatResponse.class)).thenReturn(receiverUserR);
        when(modelMapper.map(testUser, AppUserChatResponse.class)).thenReturn(testUserR);
        Pageable pageable = PageRequest.of(0,10);
        List<ChatResponseList> result = chatFacade.getAllUserChats(pageable);

        assertEquals(List.of(chatResponse, chatResponse1), result);
        assertEquals(2, result.size());
    }
    @Test
    void testGetChatById(){
        when(chatService.findChatById(1L)).thenReturn(chat);
        when(appUserService.getAuthUser()).thenReturn(authUser);

        when(modelMapper.map(chat, ChatResponse.class)).thenReturn(chatR);
        when(modelMapper.map(receiverUser, AppUserChatResponse.class)).thenReturn(receiverUserR);

        ChatResponse chatResponseResult = chatFacade.getChatById(1L);

        assertNotNull(chatResponseResult);
        assertEquals(chatR, chatResponseResult);
        assertEquals(1, chatResponseResult.getId());
        assertEquals(chatR.getChatParticipant(), chatResponseResult.getChatParticipant());
    }
    @Test
    void testGetChatByIdWithNotFoundException(){
        Long chatId = 1L;
        when(chatService.findChatById(chatId)).thenReturn(chat);
        when(appUserService.getAuthUser()).thenReturn(testUser);

        assertThrows(NotFoundException.class, () -> chatFacade.getChatById(chatId));
    }
    @Test
    void testGetReceiverUser(){
        Long chatId = 1L;
        when(appUserService.getAuthUser()).thenReturn(authUser);
        when(chatService.getReceiverUser(authUser, chatId)).thenReturn(receiverUser);

        AppUser result = chatFacade.getReceiverUser(chatId);

        assertNotNull(result);
        assertEquals(receiverUser, result);
    }
    @Test
    void testGetReceiverUserWithPrincipal(){
        Long chatId = 1L;
        Principal principal = mock(Principal.class);
        when(appUserService.getAuthUser(principal)).thenReturn(authUser);
        when(chatService.getReceiverUser(authUser, chatId)).thenReturn(receiverUser);

        AppUser result = chatFacade.getReceiverUser(chatId, principal);

        assertNotNull(result);
        assertEquals(receiverUser, result);
    }

    @Test
    void testSearchUser(){
        String input = "name";
        when(appUserService.getAuthUser()).thenReturn(authUser);
        List<Map<String, Object>> sqlResults = new ArrayList<>();
        Map<String, Object> result1 = new HashMap<>();
        result1.put("userId", 2L);
        sqlResults.add(result1);
        ChatSqlResult chatSqlResult1 = new ChatSqlResult(){{setUserId(2L);}};

        when(modelMapper.map(result1, ChatSqlResult.class)).thenReturn(chatSqlResult1);
        when(modelMapper.map(chatSqlResult1, AppUserChatResponse.class)).thenReturn(receiverUserR);
        ChatResponse chatResponse1 = new ChatResponse();
        chatResponse1.setChatParticipant(receiverUserR);
        when(modelMapper.map(chatSqlResult1, ChatResponse.class)).thenReturn(chatResponse1);

        when(chatService.searchChats(eq(authUser), eq(input), any(Pageable.class))).thenReturn(sqlResults);

        when(modelMapper.map(receiverUser, AppUserChatResponse.class)).thenReturn(receiverUserR);
        Pageable pageable = PageRequest.of(0,10);
        List<ChatResponse> result = chatFacade.searchChats(input, pageable);

        assertEquals(1, result.size());
        assertEquals(receiverUserR, result.get(0).getChatParticipant());
        assertEquals(receiverUserR.getId(), result.get(0).getChatParticipant().getId());
    }
}
