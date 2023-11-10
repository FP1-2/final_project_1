package com.facebook.service;

import com.facebook.model.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
@Log4j2
@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    public void sendNotification(AppUser user) {
        sendMessageToUser(user, "/queue/notification", 1);
    }
    public void sendNewMessage(AppUser user, Object message) {
        sendMessageToUser(user, "/queue/messages", message);
    }
    public void sendNewMessage(Principal user, Object message) {
        sendMessageToAuthUser(user, "/queue/messages", message);
    }
    public void sendMessageNotification(AppUser user, Object message) {
        sendMessageToUser(user, "/queue/messageNotification", message);
    }
    public void sendMessageNotification(Principal user, Object message) {
        sendMessageToAuthUser(user, "/queue/messageNotification", message);
    }
    public void updateMessageStatus(AppUser user, Object message) {
        sendMessageToUser(user, "/queue/messageStatus", message);
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
