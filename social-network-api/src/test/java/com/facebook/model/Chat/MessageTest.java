package com.facebook.model.Chat;

import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.model.chat.ContentType;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class MessageTest {
    @Autowired
    private TestEntityManager entityManager;
    private AppUser sender;
    private Chat chat;
    @BeforeEach
    void setUp(){
        sender = new AppUser();
        sender.setEmail("test");
        sender.setName("test");
        sender.setSurname("test");
        sender.setUsername("test");
        sender.setPassword("test");
        chat = new Chat();
    }
    @Test
    void testSaveMessage(){
        entityManager.persist(sender);
        entityManager.persist(chat);
        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContentType(ContentType.LIKE);
        message.setStatus(MessageStatus.READ);
        entityManager.persist(message);

        Message retrievedMessage = entityManager.find(Message.class, message.getId());
        assertNotNull(retrievedMessage);
        assertNotNull(retrievedMessage.getId());
        assertEquals(ContentType.LIKE, retrievedMessage.getContentType());
        assertEquals(MessageStatus.READ, retrievedMessage.getStatus());
    }

    @Test
    void testUpdateMessage(){
        entityManager.persist(sender);
        entityManager.persist(chat);
        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContentType(ContentType.LIKE);
        message.setStatus(MessageStatus.READ);
        entityManager.persist(message);

        LocalDateTime newModifiedDate= LocalDateTime.now().withNano(0);
        message.setLastModifiedDate(newModifiedDate);
        entityManager.flush();
        Message retrievedMessage = entityManager.find(Message.class, message.getId());
        assertNotNull(retrievedMessage);
        assertEquals(newModifiedDate, retrievedMessage.getLastModifiedDate().withNano(0));
    }

    @Test
    void testCreateMessageOf(){
        entityManager.persist(sender);
        entityManager.persist(chat);
        Message message = Message.of(ContentType.TEXT,"text", sender, chat, MessageStatus.SENT);
        entityManager.persist(message);

        Message retrievedMessage = entityManager.find(Message.class, message.getId());
        assertNotNull(retrievedMessage);
        assertEquals(ContentType.TEXT, retrievedMessage.getContentType());
        assertEquals(MessageStatus.SENT, retrievedMessage.getStatus());
        assertEquals("text", retrievedMessage.getContent());
        assertEquals(sender.getId(), retrievedMessage.getSender().getId());
        assertEquals(chat.getId(), retrievedMessage.getChat().getId());
    }
}
