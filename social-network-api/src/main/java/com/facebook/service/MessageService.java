package com.facebook.service;

import com.facebook.dto.message.MessageRequest;
import com.facebook.dto.message.MessageResponse;
import com.facebook.exception.UserNotFoundException;
import com.facebook.facade.MessageFacade;
import com.facebook.model.AppUser;
import com.facebook.model.Chat;
import com.facebook.model.Message;
import com.facebook.model.MessageStatus;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.MessageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageFacade facade;
    private Message saveMessage(Message message){
        return messageRepository.save(message);
    }
    public MessageResponse addMessage(MessageRequest messRq){
        AppUser authUser = appUserService.getAuthUser();
        Message message = facade.convertToMessage(messRq);
        Chat chatById = chatService.findChatById(messRq.getChatId());
        Message newMessage = new Message(message.getText(), message.getSender(), chatById, MessageStatus.SENT);
        Message savedMessage = saveMessage(newMessage);
        return facade.convertToMessageResponse(savedMessage);
    }

    public List<MessageResponse> getAllMessages(Long chatId, Pageable pageable){
        Page<Message> listOfMess = messageRepository.findAllByChatIdOrderByCreatedDateDesc(chatId, pageable);
        return listOfMess.stream()
                .map(m -> facade.convertToMessageResponse(m))
                .toList();
    }
    public MessageResponse updateStatus(Long id, MessageStatus status){
        Optional<Message> mess = messageRepository.findById(id);
        if(mess.isPresent()) {
            mess.get().setStatus(status);
            Message savedMessage = saveMessage(mess.get());
            return facade.convertToMessageResponse(savedMessage);
        }
        throw new UserNotFoundException();
    }
}
