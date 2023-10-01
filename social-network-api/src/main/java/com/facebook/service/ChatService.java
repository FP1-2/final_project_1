package com.facebook.service;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.chat.ChatRequest;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.facade.ChatFacade;
import com.facebook.model.AppUser;
import com.facebook.model.Chat;
import com.facebook.model.MessageStatus;
import com.facebook.repository.ChatRepository;
import com.facebook.repository.MessageRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private ChatFacade facade;
    public List<ChatResponseList> getAllUserChats(){
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        Optional<AppUser> activeUser = appUserService.findByUsername(principal.getName());
        return activeUser.map(u -> {
            return chatRepository.findByChatParticipantsContaining(u)
                    .stream()
                    .map(facade::convertToChatResponseList)
                    .toList();
        })
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
    public Chat findChatById(long id){
        return chatRepository.findById(id).map(c->c)
                .orElseThrow(()-> new UsernameNotFoundException("chat not found"));
    }
    public ChatResponse chatById(ChatRequest chatRq){
        Chat chat = facade.convertToChat(chatRq);
        Optional<Chat> chatById = chatRepository.findById(chat.getId());
        return chatById.map(c -> facade.convertToChatResponse(c))
                .orElseThrow(()-> new UsernameNotFoundException("chat not found"));
    }
    public ChatResponse chatById(Long id){
        Optional<Chat> chatById = chatRepository.findById(id);
        return chatById.map(c -> facade.convertToChatResponse(c))
                .orElseThrow(()-> new UsernameNotFoundException("chat not found"));
    }
    public AppUser getRecieverUser(Long id){
        Chat chat = findChatById(id);
        AppUser authUser = appUserService.getAuthUser();
        List<AppUser> chatParticipants = chat.getChatParticipants();
        if(Objects.equals(chatParticipants.get(0).getId(), authUser.getId())){
            return chatParticipants.get(1);
        }
            return chatParticipants.get(0);
    }
    public AppUserChatResponse getRecieverUser(ChatRequest chatRq){

        AppUser authUser = appUserService.getAuthUser();
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        ChatResponse chatResponse = chatById(chatRq);
        List<AppUserChatResponse> chatParticipants = chatResponse.getChatParticipants();
        AppUserChatResponse appUserChatResponse = chatParticipants.get(0);
        if(appUserChatResponse.getUsername().equals(principal.getName())){
            appUserChatResponse = chatParticipants.get(1);
        }
        return appUserChatResponse;
    }
    public ChatResponse createNewChat(String username){
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        Optional<AppUser> activeUser = appUserService.findByUsername(principal.getName());
        if(activeUser.isEmpty()) throw new UsernameNotFoundException("User not found");
        Optional<AppUser> chatParticipant = appUserService.findByUsername(username);
        if(chatParticipant.isEmpty()) throw new UsernameNotFoundException("User not found");

        List<Chat> chats = chatRepository.findChatsByChatParticipantsContainsAndChatParticipantsContains(activeUser.get(), chatParticipant.get());
        if(chats.isEmpty()){
            Chat newChat = new Chat();
            newChat.addChatParticipants(activeUser.get(), chatParticipant.get());
            newChat.addMess(activeUser.get(), newChat, MessageStatus.READ);

            chatRepository.save(newChat);
            return facade.convertToChatResponse(newChat);
        } else{
            return facade.convertToChatResponse(chats.get(0));
        }
    }
}
