package com.facebook.service;

import com.facebook.dto.message.MessageRequest;
import com.facebook.dto.message.MessageResponse;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.MessageFacade;
import com.facebook.model.AppUser;
import com.facebook.model.Message;
import com.facebook.model.MessageStatus;
import com.facebook.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final AppUserService appUserService;
    private final MessageFacade facade;
    private Message saveMessage(Message message){
        return messageRepository.save(message);
    }
    public MessageResponse addMessage(MessageRequest messRq){
        AppUser authUser = appUserService.getAuthUser();
        Message message = facade.convertToMessage(messRq);
        Message newMessage = new Message(message.getText(), authUser, message.getChat(), MessageStatus.SENT);
        Message savedMessage = saveMessage(newMessage);
        return facade.convertToMessageResponse(savedMessage);
    }

    public List<MessageResponse> getAllMessages(Long chatId, Pageable pageable){
        Page<Message> listOfMess = messageRepository.findAllByChatIdOrderByCreatedDateDesc(chatId, pageable);
        return listOfMess.stream()
                .map(facade::convertToMessageResponse)
                .toList();
    }
    public MessageResponse updateStatus(Long id, MessageStatus status){
        Message mess = findById(id);
        mess.setStatus(status);
        Message savedMessage = saveMessage(mess);

        return facade.convertToMessageResponse(savedMessage);
    }
    public Message findById(Long id){
        return messageRepository.findById(id).orElseThrow(()->new NotFoundException("Message not found"));
    }
}
