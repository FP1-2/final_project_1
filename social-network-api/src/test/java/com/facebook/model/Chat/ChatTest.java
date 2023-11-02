package com.facebook.model.Chat;

import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChatTest {
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveChat(){
        Chat chat = new Chat();
        entityManager.persist(chat);

        Chat retrievedChat = entityManager.find(Chat.class, chat.getId());
        assertNotNull(retrievedChat);
        assertNotNull(retrievedChat.getId());
        assertTrue(retrievedChat.getId()>0);
    }

    @Test
    void testUpdateChat(){
        Chat chat = new Chat();
        entityManager.persist(chat);

        LocalDateTime newModifiedDate= LocalDateTime.now().withNano(0);
        chat.setLastModifiedDate(newModifiedDate);
        entityManager.flush();
        Chat retrievedChat = entityManager.find(Chat.class, chat.getId());
        assertNotNull(retrievedChat);
        assertEquals(newModifiedDate, retrievedChat.getLastModifiedDate().withNano(0));
    }


    @Test
    void testCreateChatOfWithChatParticipant(){
        AppUser appUser1 = new AppUser();
        AppUser appUser2 = new AppUser();
        Chat chat = Chat.of(appUser1, appUser2);
        entityManager.persist(chat);

        Chat retrievedChat = entityManager.find(Chat.class, chat.getId());
        assertNotNull(retrievedChat);
        assertEquals(appUser1, retrievedChat.getChatParticipants().get(0));
        assertEquals(appUser2, retrievedChat.getChatParticipants().get(1));
    }
}
