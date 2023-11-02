package com.facebook.service;

import com.facebook.exception.NotFoundException;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.model.chat.ContentType;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;
import com.facebook.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MessageService.class)
class MessageServiceTest {
    @MockBean
    private MessageRepository messageRepository;
    @Autowired
    private MessageService messageService;

    @Test
    void testFindById() {
        Long messageId = 1L;
        Message expectedMessage = new Message();
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(expectedMessage));

        Message actualMessage = messageService.findById(messageId);

        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void testFindByIdWithNotFoundException() {
        Long messageId = 1L;
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> messageService.findById(messageId));
    }
    @Test
    void testGetAllMessages() {
        Long chatId = 1L;
        Pageable pageable = PageRequest.of(0,10);

        List<Message> messageList = List.of(new Message(), new Message());
        when(messageRepository.findAllByChatIdOrderByCreatedDateDesc(chatId, pageable)).thenReturn(new PageImpl<>(messageList));

        Page<Message> result = messageService.getAllMessages(chatId, pageable);
        assertEquals(messageList.size(), result.getContent().size());

    }

    @Test
    void testAddMessage() {
        Message newMessage = Message.of(ContentType.TEXT, "testContent", new AppUser(), new Chat(), MessageStatus.SENT);
        when(messageRepository.save(newMessage)).thenReturn(newMessage);

        Message savedMessage = messageService.addMessage(newMessage);
        assertEquals(MessageStatus.SENT, savedMessage.getStatus());
    }

    @Test
    void testUpdateStatus() {
        Long messageId = 1L;
        Message message = Message.of(ContentType.TEXT, "testContent", new AppUser(), new Chat(), MessageStatus.SENT);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        MessageStatus newStatus = MessageStatus.READ;
        message.setStatus(newStatus);
        when(messageRepository.save(message)).thenReturn(message);
        Message updatedMessage = messageService.updateStatus(messageId, newStatus);

        assertEquals(newStatus, updatedMessage.getStatus());
    }
    @Test
    void testUpdateStatusMessageNotFound() {
        Long messageId = 1L;
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> messageService.updateStatus(messageId, MessageStatus.READ));
    }
    @Test
    void testCountUnreadMessage() {
        AppUser user = new AppUser();
        user.setId(1L);
        AppUser sender = new AppUser();
        sender.setId(2L);
        Chat chat = new Chat();
        Message message1 = Message.of(ContentType.TEXT, "testContent", sender, chat, MessageStatus.SENT);
        Message message2 = Message.of(ContentType.IMAGE, "testContent", sender, chat, MessageStatus.SENT);

        when(messageRepository.findAllByStatusEqualsAndChat_ChatParticipantsContaining(
                MessageStatus.SENT, user)).thenReturn(List.of(message1, message2));

        Long unreadMessageCount = messageService.countUnreadMessage(user);

        assertEquals(2, unreadMessageCount);
    }
}
