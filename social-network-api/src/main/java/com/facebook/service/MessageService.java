package com.facebook.service;

import com.facebook.exception.NotFoundException;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;
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
        Message newMessage = Message.of(message.getContentType(),
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
}
