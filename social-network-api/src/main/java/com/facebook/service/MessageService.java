package com.facebook.service;

import com.facebook.dto.message.MessageRequest;
import com.facebook.dto.message.MessageResponse;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.MessageFacade;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.model.chat.ContentType;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;
import com.facebook.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
//    private final AppUserService appUserService;
//    private final ChatService chatService;
//    private final MessageFacade facade;
    private Message saveMessage(Message message){
        return messageRepository.save(message);
    }
    public Message findById(Long id){
        return messageRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Message not found"));
    }
    public Page<Message> getAllMessages(Long chatId, Pageable pageable){
        return messageRepository.findAllByChatIdOrderByCreatedDateDesc(chatId, pageable);
    }
    public Message addMessage(Message message){
        Message newMessage = new Message(message.getContentType(),
                message.getContent(),
                message.getSender(),
                message.getChat(),
                MessageStatus.SENT);
        return saveMessage(newMessage);
    }
    public Message updateStatus(Long id, MessageStatus status){
        Message updatedMessage = findById(id);
        updatedMessage.setStatus(status);
        return saveMessage(updatedMessage);
    }
    public Long countUnreadMessage(AppUser user) {
        List<Message> messages = messageRepository.findAllByStatusEqualsAndChat_ChatParticipantsContaining(MessageStatus.SENT, user);
        return messages.stream().filter(m -> !m.getSender().equals(user)).count();
    }

//    public List<MessageResponse> getAllMessages(Long chatId, Pageable pageable){
//        Page<Message> listOfMess = messageRepository.findAllByChatIdOrderByCreatedDateDesc(chatId, pageable);
//        AppUser chatP = chatService.getReceiverUser(chatId);
//        return listOfMess.stream()
//                .map(m -> facade.convertToMessageResponse(m, chatP))
//                .toList();
//    }

//    public MessageResponse addMessage(MessageRequest messRq, Principal principal){
//
//        AppUser authUser = appUserService.getAuthUser(principal);
//        AppUser chatP = chatService.getReceiverUser(messRq.getChatId(), principal);
//        AppUser sender = appUserService.convertToAppUser(principal.getName());
//        Message message = facade.convertToMessage(messRq, sender);
//
//        Message newMessage = new Message(message.getContentType() ,message.getContent(), authUser, message.getChat(), MessageStatus.SENT);
//
//        Message savedMessage = saveMessage(newMessage);
//
//        chatService.updateLastModifiedDate(savedMessage.getChat(), savedMessage.getLastModifiedDate());
//        return facade.convertToMessageResponse(savedMessage, chatP);
//    }


//    public MessageResponse updateStatus(Long id, MessageStatus status, Principal principal){
//        Message mess = findById(id);
//        mess.setStatus(status);
//        Message savedMessage = saveMessage(mess);
//        AppUser chatP = chatService.getReceiverUser(mess.getChat().getId(), principal);
//        return facade.convertToMessageResponse(savedMessage, chatP);
//    }

}
