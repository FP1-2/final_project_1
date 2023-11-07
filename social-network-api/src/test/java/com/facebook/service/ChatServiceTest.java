package com.facebook.service;

import com.facebook.exception.NotFoundException;
import com.facebook.exception.UserNotFoundException;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.repository.ChatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = ChatService.class)
class ChatServiceTest {
    @MockBean
    private ChatRepository chatRepository;
    @Autowired
    private ChatService chatService;
    @Test
    void testSaveChat(){
        Chat chat = new Chat();
        when(chatRepository.save(chat)).thenReturn(chat);
        Chat savedChat = chatService.saveChat(chat);
        assertEquals(chat, savedChat);
    }
    @Test
    void testFindChatById(){
        Long chatId = 1L;
        Chat chat = new Chat();
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        Chat foundChat = chatService.findChatById(chatId);
        assertEquals(chat, foundChat);
    }
    @Test
    void testFindChatByIdWithNotFoundException() {
        Long chatId = 1L;

        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> chatService.findChatById(chatId));
    }
    @Test
    void testGetAllUserChats(){
        Pageable pageable = PageRequest.of(0,10);
        AppUser authUser = new AppUser();
        List<Chat> chatList = Arrays.asList(new Chat(), new Chat());
        when(chatRepository.findByChatParticipantsContainingOrderByLastModifiedDateDesc(authUser, pageable)).thenReturn(new PageImpl<>(chatList));
        Page<Chat> result = chatService.getAllUserChats(authUser, pageable);
        assertEquals(chatList.size(), result.getContent().size());
    }
    @Test
    void testGetReceiverUser() {
        AppUser authUser = new AppUser();
        authUser.setId(1L);
        AppUser chatParticipant = new AppUser();
        chatParticipant.setId(2L);
        Long chatId = 12L;
        Chat chat = Chat.of(authUser, chatParticipant);
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        AppUser receiverUser = chatService.getReceiverUser(authUser, chatId);

        assertEquals(chatParticipant, receiverUser);
    }
    @Test
    void testGetReceiverUserWithUserNotFoundException() {
        AppUser authUser = new AppUser();
        authUser.setId(1L);
        Long chatId = 1L;
        Chat chat = new Chat();
        chat.setChatParticipants(List.of(authUser));
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        assertThrows(UserNotFoundException.class, () -> chatService.getReceiverUser(authUser, chatId));
    }
    @Test
    void testGetReceiverUserWithChatNotContainsAuthUser() {
        AppUser authUser = new AppUser();
        authUser.setId(1L);
        AppUser chatParticipant = new AppUser();
        chatParticipant.setId(2L);
        AppUser testUser = new AppUser();
        testUser.setId(3L);
        Long chatId = 1L;
        Chat chat = Chat.of(testUser, chatParticipant);
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));

        assertThrows(UserNotFoundException.class, () -> chatService.getReceiverUser(authUser, chatId));
    }
    @Test
    void testCreateNewChat() {
        AppUser authUser = new AppUser();
        AppUser chatParticipant = new AppUser();
        Chat newChat =  Chat.of(authUser, chatParticipant);
        when(chatRepository.findChatsByChatParticipantsContainsAndChatParticipantsContains(authUser, chatParticipant)).thenReturn(Collections.emptyList());
        when(chatRepository.save(newChat)).thenReturn(newChat);

        Chat createdChat = chatService.createNewChat(authUser, chatParticipant);

        assertEquals(newChat, createdChat);
    }
    @Test
    void testCreateNewChatIfChatIsExist() {
        AppUser authUser = new AppUser();
        AppUser chatParticipant = new AppUser();
        Chat chat =  new Chat();
        List<Chat> chatList = List.of(chat);
        when(chatRepository.findChatsByChatParticipantsContainsAndChatParticipantsContains(authUser, chatParticipant)).thenReturn(chatList);

        Chat createdChat = chatService.createNewChat(authUser, chatParticipant);

        assertEquals(chat, createdChat);
    }
    @Test
    void testSearchChats() {
        Pageable pageable = PageRequest.of(0, 10);
        AppUser authUser = new AppUser();
        String input = "testName";
        List<Map<String, Object>> resultData = Arrays.asList(new HashMap<>(), new HashMap<>());

        when(chatRepository.findChatsByKeyword(authUser.getId(), input, pageable)).thenReturn(resultData);

        List<Map<String, Object>> searchResult = chatService.searchChats(authUser, input, pageable);

        assertEquals(resultData.size(), searchResult.size());
    }
    @Test
    void testUpdateLastModifiedDate() {
        Long chatId = 1L;
        LocalDateTime updatedDate = LocalDateTime.now();
        Chat chat = new Chat();
        chat.setId(chatId);
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));
        when(chatRepository.save(chat)).thenReturn(chat);
        Chat updatedChat = chatService.updateLastModifiedDate(chat, updatedDate);

        assertEquals(updatedDate, updatedChat.getLastModifiedDate());

    }
}
