package com.facebook.facade;


import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.message.MessageRequest;
import com.facebook.dto.message.MessageResponse;
import com.facebook.dto.message.MessageResponseList;
import com.facebook.model.AppUser;
import com.facebook.model.Message;
import com.facebook.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class MessageFacade {

    private final ModelMapper modelMapper;
    private final AppUserFacade userFacade;

    public MessageResponse convertToMessageResponse(Message message) {
        return modelMapper.map(message, MessageResponse.class);
    }


    public Message convertToMessage(MessageRequest messageRequest) {
        Message message = modelMapper.map(messageRequest, Message.class);
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        AppUser sender = userFacade.convertToAppUser(principal.getName());
        message.setSender(sender);

        return message;
    }
    public MessageResponseList convertToMessageResponseList(Message message) {
        AppUser sender = message.getSender();
        AppUserChatResponse appUserResponse = userFacade.convertToAppUserChatResponse(sender);
        MessageResponseList map = modelMapper.map(message, MessageResponseList.class);
        map.setSender(appUserResponse);
        return map;
    }

}