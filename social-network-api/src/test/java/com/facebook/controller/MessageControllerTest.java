package com.facebook.controller;

import com.facebook.TestConfig;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.message.MessageResponse;
import com.facebook.dto.message.MessageRequest;
import com.facebook.facade.ChatFacade;
import com.facebook.facade.MessageFacade;
import com.facebook.model.AppUser;
import com.facebook.model.chat.MessageStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MessageController.class)
@Import(TestConfig.class)
class MessageControllerTest {

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @MockBean
    private MessageFacade messageFacade;

    @MockBean
    private ChatFacade chatFacade;
    @Autowired
    private MessageController messageController;
    @Test
    void testSendMessage() {

        MessageRequest messageRequest = new MessageRequest();
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        Principal principal = mock(Principal.class);
        when(headerAccessor.getUser()).thenReturn(principal);

        AppUser receiverUser = new AppUser();
        MessageResponse messageResponse = new MessageResponse();
        when(chatFacade.getReceiverUser(any(), any())).thenReturn(receiverUser);
        when(messageFacade.addMessage(any(), any())).thenReturn(messageResponse);
        when(messageFacade.countUnreadMessage(any(Principal.class))).thenReturn(1L);

        messageController.sendMessage(messageRequest, headerAccessor);

        verify(messageFacade, times(1)).addMessage(messageRequest, principal);
        verify(messageFacade, times(1)).countUnreadMessage(receiverUser);
    }
    @Test
    void testUpdateMessageStatus() {

        Long messageId = 1L;
        String newStatus = "READ";
        SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
        Principal principal = mock(Principal.class);
        when(headerAccessor.getUser()).thenReturn(principal);

        MessageResponse messageResponse = new MessageResponse(){{setId(messageId);
                                                                setStatus(MessageStatus.READ);
                                                                setChat(new ChatResponse(){
                                                                    {setId(2L);}});}};
        AppUser receiverUser = new AppUser(){{setUsername("testUsername");}};
        when(chatFacade.getReceiverUser(any(), any())).thenReturn(receiverUser);
        when(messageFacade.updateStatus(any(), any(), any())).thenReturn(messageResponse);

        messageController.updateMessageStatus(messageId, newStatus, headerAccessor);

        verify(messageFacade, times(1)).updateStatus(messageId, MessageStatus.READ, principal);
        verify(chatFacade, times(1)).getReceiverUser(any(), any());
        verify(messagingTemplate, times(1)).convertAndSendToUser(receiverUser.getUsername(), "/queue/messageStatus", messageResponse);
        verify(messageFacade, times(1)).countUnreadMessage(principal);
    }
}
