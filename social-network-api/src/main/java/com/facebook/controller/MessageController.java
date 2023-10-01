package com.facebook.controller;

import com.facebook.dto.message.MessageRequest;
import com.facebook.dto.message.MessageResponse;
import com.facebook.model.AppUser;
import com.facebook.model.MessageStatus;
import com.facebook.service.ChatService;
import com.facebook.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/messages/{chatId}")
    public void getAllMessages(@DestinationVariable Long chatId,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        List<MessageResponse> allMessages = messageService.getAllMessages(chatId, pageable);
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/chat/", allMessages);

    }
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessageRequest messRq){
        MessageResponse messageResponse = messageService.addMessage(messRq);
        AppUser recieverUser = chatService.getRecieverUser(messRq.getChatId());
        try{
            simpMessagingTemplate.convertAndSendToUser(recieverUser.getUsername(), "/queue/private", messageResponse);
        } catch(Exception ex){
            MessageResponse res = messageService.updateStatus(messageResponse.getId(), MessageStatus.FAILED);
            Principal principal = SecurityContextHolder.getContext().getAuthentication();
            simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/private", res);
        }



    }
    @MessageMapping("/updateMessageStatus/{messageId}")
    public void updateMessageStatus(@DestinationVariable Long messageId, String newStatus) {
        MessageResponse messageResponse = messageService.updateStatus(messageId, MessageStatus.valueOf(newStatus));
        AppUser recieverUser = chatService.getRecieverUser(messageResponse.getChat().getId());
        simpMessagingTemplate.convertAndSendToUser(recieverUser.getUsername(), "/queue/private", messageResponse);
    }
}
