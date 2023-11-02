package com.facebook.controller;

import com.facebook.dto.message.MessageRequest;
import com.facebook.dto.message.MessageResponse;
import com.facebook.facade.ChatFacade;
import com.facebook.facade.MessageFacade;
import com.facebook.model.AppUser;
import com.facebook.model.chat.MessageStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Log4j2
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageFacade messageFacade;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatFacade chatFacade;
    private final String messageNotificationPath = "/queue/notifications";

    @MessageMapping("/chat")
    public void sendMessage(@Payload MessageRequest messRq, SimpMessageHeaderAccessor headerAccessor){
        String sendMessagePath = "/queue/messages";
        Principal principal = headerAccessor.getUser();
        AppUser receiverUser = chatFacade.getReceiverUser(messRq.getChatId(), principal);
        MessageResponse messageResponse = messageFacade.addMessage(messRq, principal);

        try{
            Long unreadMessage = messageFacade.countUnreadMessage(receiverUser);

            sendMessageToUser(receiverUser, sendMessagePath, messageResponse);
            sendMessageToUser(receiverUser, messageNotificationPath, unreadMessage);
        } catch(Exception ex){
            MessageResponse res = messageFacade.updateStatus(messageResponse.getId(), MessageStatus.FAILED, principal);
            sendMessageToAuthUser(principal, sendMessagePath, res);
            log.error("Failed status of sending message with error: "+ ex);
        }
        sendMessageToAuthUser(principal, sendMessagePath, messageResponse);
    }
    @MessageMapping("/updateMessageStatus/{messageId}")
    public void updateMessageStatus(@DestinationVariable Long messageId, String newStatus, SimpMessageHeaderAccessor headerAccessor) {
        MessageResponse messageResponse = messageFacade.updateStatus(messageId, MessageStatus.valueOf(newStatus), headerAccessor.getUser());
        AppUser receiverUser = chatFacade.getReceiverUser(messageResponse.getChat().getId(), headerAccessor.getUser());
        String updateStatusPath = "/queue/messageStatus";
        messagingTemplate.convertAndSendToUser(receiverUser.getUsername(), updateStatusPath, messageResponse);
        Principal principal = headerAccessor.getUser();
        Long unreadMessage = messageFacade.countUnreadMessage(principal);
        log.info("User have "+unreadMessage +" messages");
        sendMessageToAuthUser(principal, messageNotificationPath, unreadMessage);
    }
    private void sendMessageToUser(AppUser user, String destination, Object message) {
        try {
            messagingTemplate.convertAndSendToUser(user.getUsername(), destination, message);
        } catch (Exception ex) {
            log.error("Failed to send message to user: " + user.getUsername(), ex);
        }
    }
    private void sendMessageToAuthUser(Principal user, String destination, Object message) {
        try {
            messagingTemplate.convertAndSendToUser(user.getName(), destination, message);
        } catch (Exception ex) {
            log.error("Failed to send message to authUser: " + user.getName(), ex);
        }
    }
}
