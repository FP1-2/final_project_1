package com.facebook.service;

import com.facebook.exception.NotFoundException;
import com.facebook.exception.UserNotFoundException;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Chat saveChat(Chat c){
        return chatRepository.save(c);
    }
    public Chat updateLastModifiedDate(Chat chat, LocalDateTime updatedDate){
        Chat chatById = findChatById(chat.getId());
        chatById.setLastModifiedDate(updatedDate);
        return saveChat(chatById);
    }
    public Chat findChatById(Long id){
        return chatRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Chat not found in DB."));
    }
    public Page<Chat> getAllUserChats(AppUser authUser, Pageable pageable){
        return chatRepository.findByChatParticipantsContainingOrderByLastModifiedDateDesc(authUser, pageable);

    }
    private Optional<AppUser> getChatParticipant(List<AppUser> chatParticipants, AppUser authUser){
        return chatParticipants.stream()
                .filter(user -> !Objects.equals(user.getId(), authUser.getId()))
                .findFirst();
    }
    public AppUser getReceiverUser(AppUser authUser, Long chatId){
        Chat chat = findChatById(chatId);
        List<AppUser> chatParticipants = chat.getChatParticipants();
        if (!chatParticipants.contains(authUser)) throw new UserNotFoundException();

        return getChatParticipant(chatParticipants,authUser).orElseThrow(UserNotFoundException::new);
    }
    public Chat createNewChat(AppUser authUser, AppUser chatParticipant){
        List<Chat> chats = chatRepository.findChatsByChatParticipantsContainsAndChatParticipantsContains(authUser, chatParticipant);
        if(!chats.isEmpty()) return chats.get(0);

        Chat newChat = Chat.of(authUser, chatParticipant);
        return chatRepository.save(newChat);
    }
    public List<Map<String, Object>> searchChats(AppUser authUser, String input, Pageable pageable){
        return chatRepository.findChatsByKeyword(authUser.getId(), input, pageable);
    }

}
