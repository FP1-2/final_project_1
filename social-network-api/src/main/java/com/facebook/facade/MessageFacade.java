package com.facebook.facade;


import com.facebook.dto.message.MessageRequest;
import com.facebook.dto.message.MessageResponse;
import com.facebook.model.AppUser;
import com.facebook.model.chat.ContentType;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;
import com.facebook.service.AppUserService;
import com.facebook.service.ChatService;
import com.facebook.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class MessageFacade {

    private final ModelMapper modelMapper;
    private final ChatFacade chatFacade;
    private final MessageService messageService;
    private final ChatService chatService;
    private final AppUserService appUserService;
    private MessageResponse convertToMessageResponse(Message message, AppUser chatParticipant) {
        MessageResponse response = modelMapper.map(message, MessageResponse.class);

        response.setChat(chatFacade.convertToChatResponse(message.getChat(), chatParticipant));
        ZonedDateTime zonedDateTime = ZonedDateTime.of(message.getCreatedDate(), ZoneOffset.UTC);
        response.setCreatedAt(zonedDateTime);
        return response;
    }

    private Message convertToMessage(MessageRequest messageRequest, AppUser sender ) {
        Message message = modelMapper.map(messageRequest, Message.class);
        message.setSender(sender);
        message.setContentType(ContentType.fromString(messageRequest.getContentType()));
        return message;
    }


    public List<MessageResponse> getAllMessages(Long chatId, Pageable pageable){
        AppUser chatParticipant = chatFacade.getReceiverUser(chatId);
        return messageService.getAllMessages(chatId, pageable).stream()
                .map(m -> convertToMessageResponse(m, chatParticipant))
                .toList();
    }

    public MessageResponse addMessage(MessageRequest messRq, Principal principal){

        AppUser chatParticipant = chatFacade.getReceiverUser(messRq.getChatId(), principal);
        AppUser sender = appUserService.convertToAppUser(principal.getName());

        Message newMessage = convertToMessage(messRq, sender);
        Message savedMessage = messageService.addMessage(newMessage);
        chatService.updateLastModifiedDate(savedMessage.getChat(), savedMessage.getLastModifiedDate());
        return convertToMessageResponse(savedMessage, chatParticipant);
    }

    public MessageResponse updateStatus(Long id, MessageStatus status, Principal principal){
        Message updatedMessage = messageService.updateStatus(id, status);
        AppUser chatParticipant = chatFacade.getReceiverUser(updatedMessage.getChat().getId(), principal);
        return convertToMessageResponse(updatedMessage, chatParticipant);
    }
    public Long countUnreadMessage(AppUser user){
        return messageService.countUnreadMessage(user);
    }
    public Long countUnreadMessage(){
        AppUser user = appUserService.getAuthUser();
        return messageService.countUnreadMessage(user);
    }
    public Long countUnreadMessage(Principal principal){
        AppUser authUser = appUserService.getAuthUser(principal);
        return messageService.countUnreadMessage(authUser);
    }

}