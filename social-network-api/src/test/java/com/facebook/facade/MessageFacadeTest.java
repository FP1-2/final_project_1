package com.facebook.facade;


import com.facebook.service.AppUserService;
import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.message.MessageRequest;
import com.facebook.dto.message.MessageResponse;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.model.chat.ContentType;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;
import com.facebook.service.ChatService;
import com.facebook.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@SpringBootTest(classes = MessageFacade.class)
class MessageFacadeTest {
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private ChatFacade chatFacade;
    @MockBean
    private MessageService messageService;
    @MockBean
    private AppUserService appUserService;
    @MockBean
    private ChatService chatService;
    @Autowired
    private MessageFacade messageFacade;
    private AppUser authUser;
    private AppUser receiverUser;
    private Chat chat;
    private ChatResponse chatR;
    private Message testMessage;
    private MessageResponse testMessageR = new MessageResponse();
    @BeforeEach
    void setUp() {
        authUser = TestDataFactory.createAuthUser();
        AppUserChatResponse authUserR = TestDataFactory.createAuthUserR();
        receiverUser = TestDataFactory.createReceiverUser();
        AppUserChatResponse receiverUserR = TestDataFactory.createReceiverUserR();
        chat = TestDataFactory.createChat(authUser, receiverUser);
        chatR = TestDataFactory.createChatResponse(receiverUserR);
        testMessage = TestDataFactory.createTestMessage(authUser, chat);
        testMessageR = TestDataFactory.createTestMessageResponse(authUserR);

    }

    @Test
    void testGetAllMessages(){
        Long chatId = 1L;
        Pageable pageable = PageRequest.of(0,10);
        when(chatFacade.getReceiverUser(chatId)).thenReturn(receiverUser);
        when(messageService.getAllMessages(chatId, pageable)).thenReturn(new PageImpl<>(List.of(testMessage)));
        when(modelMapper.map(testMessage, MessageResponse.class)).thenReturn(testMessageR);
        when(chatFacade.convertToChatResponse(chat, receiverUser)).thenReturn(chatR);
        List<MessageResponse> result = messageFacade.getAllMessages(chatId, pageable);

        assertEquals(List.of(testMessageR), result);
        assertEquals(1, result.size());

    }
    @Test
    void testAddMessage(){
        Principal principal = mock(Principal.class);
        Long chatId = 1L;
        MessageRequest messageRequest = new MessageRequest(){{
            setChatId(chatId);
            setContentType("IMAGE");
            setContent("content");
        }};

        when(chatFacade.getReceiverUser(chatId, principal)).thenReturn(receiverUser);
        when(appUserService.convertToAppUser(anyString())).thenReturn(authUser);
        when(messageService.addMessage(testMessage)).thenReturn(testMessage);

        when(modelMapper.map(messageRequest, Message.class)).thenReturn(testMessage);

        when(modelMapper.map(testMessage, MessageResponse.class)).thenReturn(testMessageR);
        when(chatFacade.convertToChatResponse(chat, receiverUser)).thenReturn(chatR);

        MessageResponse result = messageFacade.addMessage(messageRequest, principal);
        assertNotNull(result);
        assertEquals(testMessageR, result);
        assertEquals(ContentType.IMAGE, result.getContentType());
        assertEquals(MessageStatus.SENT, result.getStatus());
        assertEquals(authUser.getId(), result.getSender().getId());
        assertEquals("content", result.getContent());
    }
    @Test
    void testUpdateStatus(){
        Principal principal = mock(Principal.class);
        Long messId = 10L;
        testMessage.setStatus(MessageStatus.READ);
        testMessageR.setStatus(MessageStatus.READ);
        when(messageService.updateStatus(messId, MessageStatus.READ)).thenReturn(testMessage);
        when(chatFacade.getReceiverUser(messId, principal)).thenReturn(receiverUser);
        when(modelMapper.map(testMessage, MessageResponse.class)).thenReturn(testMessageR);
        when(chatFacade.convertToChatResponse(chat, receiverUser)).thenReturn(chatR);

        MessageResponse result = messageFacade.updateStatus(messId, MessageStatus.READ, principal);
        assertNotNull(result);
        assertEquals(testMessageR.getId(), result.getId());
        assertEquals(MessageStatus.READ, result.getStatus());
    }
    @Test
    void testCountUnreadMessageWithUser(){
        when(messageService.countUnreadMessage(authUser)).thenReturn(5L);
        Long result = messageFacade.countUnreadMessage(authUser);
        assertNotNull(result);
        assertEquals(5, result);
    }
    @Test
    void testCountUnreadMessage(){
        when(appUserService.getAuthUser()).thenReturn(authUser);
        when(messageService.countUnreadMessage(authUser)).thenReturn(2L);
        Long result = messageFacade.countUnreadMessage();
        assertNotNull(result);
        assertEquals(2, result);
    }
    @Test
    void testCountUnreadMessageWithPrincipal(){
        Principal principal = mock(Principal.class);
        when(appUserService.getAuthUser(principal)).thenReturn(authUser);
        when(messageService.countUnreadMessage(authUser)).thenReturn(3L);
        Long result = messageFacade.countUnreadMessage(principal);
        assertNotNull(result);
        assertEquals(3, result);
    }
}
