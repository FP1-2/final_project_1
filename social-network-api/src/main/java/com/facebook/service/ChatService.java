package com.facebook.service;

import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.exception.NotFoundException;
import com.facebook.exception.UserNotFoundException;
import com.facebook.facade.ChatFacade;
import com.facebook.model.AppUser;
import com.facebook.model.Chat;
import com.facebook.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final AppUserService appUserService;
    private final ChatFacade facade;
    public List<ChatResponseList> getAllUserChats(Pageable pageable){
        AppUser authUser = appUserService.getAuthUser();
        return chatRepository.findByChatParticipantsContainingOrderByLastModifiedDateDesc(authUser, pageable)
                .stream()
                .map(facade::convertToChatResponseList)
                .toList();
    }
    public Chat findChatById(Long id){
        return chatRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Chat not found in DB."));
    }
    public ChatResponse getChatById(Long id){
        Chat chat = findChatById(id);
        return facade.convertToChatResponse(chat);
    }
    public AppUser getReceiverUser(Long id){
        Chat chat = findChatById(id);
        AppUser authUser = appUserService.getAuthUser();
        List<AppUser> chatParticipants = chat.getChatParticipants();
        if(Objects.equals(chatParticipants.get(0).getId(), authUser.getId())){
            return chatParticipants.get(1);
        }
            return chatParticipants.get(0);
    }
    public ChatResponse createNewChat(String username){
        AppUser authUser = appUserService.getAuthUser();

        Optional<AppUser> chatParticipant = appUserService.findByUsername(username);
        if(chatParticipant.isEmpty()) throw new UserNotFoundException();

        List<Chat> chats = chatRepository.findChatsByChatParticipantsContainsAndChatParticipantsContains(authUser, chatParticipant.get());
        if(chats.isEmpty()){
            Chat newChat = Chat.of(authUser, chatParticipant.get());
            chatRepository.save(newChat);
            return facade.convertToChatResponse(newChat);
        }
            return facade.convertToChatResponse(chats.get(0));
    }
}
