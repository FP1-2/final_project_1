package com.facebook.service;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.dto.message.MessageResponseList;
import com.facebook.exception.NotFoundException;
import com.facebook.exception.UserNotFoundException;
import com.facebook.facade.AppUserFacade;
import com.facebook.facade.ChatFacade;
import com.facebook.facade.MessageFacade;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.model.chat.Message;
import com.facebook.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
//    private final AppUserService appUserService;
//    private final ChatFacade facade;
//    private final AppUserFacade userFacade;
//    private final MessageFacade messageFacade;

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
        return Objects.equals(chatParticipants.get(0).getId(), authUser.getId())
                ? Optional.of(chatParticipants.get(1))
                : Optional.of(chatParticipants.get(0));
    }
    public AppUser getReceiverUser(AppUser authUser, Long chatId){
        Chat chat = findChatById(chatId);
        List<AppUser> chatParticipants = chat.getChatParticipants();
        return getChatParticipant(chatParticipants,authUser).orElseThrow(() -> new UserNotFoundException());
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
//    public ChatResponse getChatById(Long id){
//        Chat chat = findChatById(id);
//        AppUser authUser = appUserService.getAuthUser();
//        if(!chat.getChatParticipants().contains(authUser)){
//            throw new NotFoundException("Chat not found.");
//        }
//        return facade.convertToChatResponse(chat, getReceiverUser(id));
//    }

//    public List<ChatResponseList> getAllUserChats(Pageable pageable){
//        AppUser authUser = appUserService.getAuthUser();
//        return chatService.getAllUserChats(authUser, pageable)
//                .stream()
//                .map(c -> {
//                    AppUserChatResponse appUserChatResponse = userFacade.convertToAppUserChatResponse(getReceiverUser(c.getId()));
//
//                    Optional<MessageResponseList> lastMess = c.getMessages().stream()
//                            .sorted(Comparator.comparing(Message::getCreatedDate).reversed())
//                            .limit(1)
//                            .map(messageFacade::convertToMessageResponseList)
//                            .findFirst();
//
//                    ChatResponseList chatResponse = facade.convertToChatResponseList(c);
//                    lastMess.ifPresent(chatResponse::setLastMessage);
//                    chatResponse.setChatParticipant(appUserChatResponse);
//                    return chatResponse;
//                })
//                .toList();
//    }

//    public AppUser getReceiverUser(Long id, Principal principal){
//        Chat chat = findChatById(id);
//        AppUser authUser = appUserService.getAuthUser(principal);
//        List<AppUser> chatParticipants = chat.getChatParticipants();
//        return getChatParticipant(chatParticipants, authUser).orElseThrow(() -> new UserNotFoundException());
//    }
//    public AppUser getReceiverUser(Long id){
//        AppUser authUser = appUserService.getAuthUser();
//        Chat chat = findChatById(id);
//        List<AppUser> chatParticipants = chat.getChatParticipants();
//        return getChatParticipant(chatParticipants,authUser).orElseThrow(() -> new UserNotFoundException());
//    }
//    public ChatResponse createNewChat(String username){
//        AppUser authUser = appUserService.getAuthUser();
//
////        Optional<AppUser> chatParticipant = appUserService.findByUsername(username);
////        if(chatParticipant.isEmpty()) throw new UserNotFoundException();
//        AppUser chatParticipant = appUserService.findByUsername(username)
//                                    .orElseThrow(UserNotFoundException::new);
//
//        List<Chat> chats = chatRepository.findChatsByChatParticipantsContainsAndChatParticipantsContains(authUser, chatParticipant);
//        if(!chats.isEmpty()) return facade.convertToChatResponse(chats.get(0), chatParticipant);
//
//        Chat newChat = Chat.of(authUser, chatParticipant);
//        chatRepository.save(newChat);
//        return facade.convertToChatResponse(newChat,chatParticipant);
//    }

//    public List<ChatResponse> searchChats(String input, Pageable pageable){
//        AppUser authUser = appUserService.getAuthUser();
//        return chatRepository.findChatsByKeyword(authUser.getId(), input, pageable)
//                .stream()
//                .map(facade::convertToChatResponseFromSql)
//                .toList();
//    }
}
