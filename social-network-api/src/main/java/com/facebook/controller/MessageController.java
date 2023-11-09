package com.facebook.controller;

import com.facebook.dto.message.MessageRequest;
import com.facebook.dto.message.MessageResponse;
import com.facebook.facade.ChatFacade;
import com.facebook.facade.MessageFacade;
import com.facebook.model.AppUser;
import com.facebook.model.chat.MessageStatus;
import com.facebook.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Log4j2
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageFacade messageFacade;
    private final ChatFacade chatFacade;
    private final WebSocketService webSocketService;

    @MessageMapping("/chat")
    public void sendMessage(@Payload MessageRequest messRq, SimpMessageHeaderAccessor headerAccessor){
        Principal principal = headerAccessor.getUser();
        AppUser receiverUser = chatFacade.getReceiverUser(messRq.getChatId(), principal);
        MessageResponse messageResponse = messageFacade.addMessage(messRq, principal);

        try{
            Long unreadMessage = messageFacade.countUnreadMessage(receiverUser);

            webSocketService.sendNewMessage(receiverUser, messageResponse);
            webSocketService.sendMessageNotification(receiverUser, unreadMessage);
        } catch(Exception ex){
            MessageResponse res = messageFacade.updateStatus(messageResponse.getId(), MessageStatus.FAILED, principal);
            webSocketService.sendNewMessage(principal, res);
            log.error("Failed status of sending message with error: "+ ex);
        }
        webSocketService.sendNewMessage(principal, messageResponse);
    }
    @MessageMapping("/updateMessageStatus/{messageId}")
    public void updateMessageStatus(@DestinationVariable Long messageId, String newStatus, SimpMessageHeaderAccessor headerAccessor) {
        MessageResponse messageResponse = messageFacade.updateStatus(messageId, MessageStatus.valueOf(newStatus), headerAccessor.getUser());
        AppUser receiverUser = chatFacade.getReceiverUser(messageResponse.getChat().getId(), headerAccessor.getUser());
        webSocketService.updateMessageStatus(receiverUser, messageResponse);

        Principal principal = headerAccessor.getUser();
        Long unreadMessage = messageFacade.countUnreadMessage(principal);
        log.info("User have "+unreadMessage +" messages");
        webSocketService.sendMessageNotification(principal, unreadMessage);
    }
}
