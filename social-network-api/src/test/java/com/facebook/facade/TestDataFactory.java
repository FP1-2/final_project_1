package com.facebook.facade;
import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.message.MessageResponse;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.model.chat.ContentType;
import com.facebook.model.chat.Message;
import com.facebook.model.chat.MessageStatus;

import java.time.LocalDateTime;
import java.util.List;


public class TestDataFactory {
    public static AppUser createAuthUser() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("authUsername");
        return user;
    }

    public static AppUserChatResponse createAuthUserR() {
        AppUserChatResponse userResponse = new AppUserChatResponse();
        userResponse.setId(1L);
        userResponse.setUsername("authUsername");
        return userResponse;
    }

    public static AppUser createReceiverUser() {
        AppUser user = new AppUser();
        user.setId(2L);
        user.setUsername("receiverUsername");
        return user;
    }

    public static AppUserChatResponse createReceiverUserR() {
        AppUserChatResponse userResponse = new AppUserChatResponse();
        userResponse.setId(2L);
        userResponse.setUsername("receiverUsername");
        return userResponse;
    }
    public static AppUser createTestUser() {
        AppUser user = new AppUser();
        user.setId(3L);
        user.setUsername("testUsername");
        return user;
    }

    public static AppUserChatResponse createTestUserR() {
        AppUserChatResponse userResponse = new AppUserChatResponse();
        userResponse.setId(3L);
        userResponse.setUsername("testUsername");
        return userResponse;
    }

    public static Chat createChat(AppUser user1, AppUser user2) {
        Chat chat = new Chat();
        chat.setId(1L);
        chat.setChatParticipants(List.of(user1, user2));
        return chat;
    }

    public static ChatResponse createChatResponse(AppUserChatResponse participant) {
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setId(1L);
        chatResponse.setChatParticipant(participant);
        return chatResponse;
    }

    public static Message createTestMessage(AppUser sender, Chat chat) {
        Message m = Message.of(ContentType.IMAGE, "content", sender, chat, MessageStatus.SENT);
        m.setCreatedDate(LocalDateTime.of(2023, 11, 6, 12, 0));
        return m;
    }

    public static MessageResponse createTestMessageResponse(AppUserChatResponse senderResponse) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setId(10L);
        messageResponse.setContentType(ContentType.IMAGE);
        messageResponse.setContent("content");
        messageResponse.setSender(senderResponse);
        messageResponse.setStatus(MessageStatus.SENT);
        return messageResponse;
    }

}
