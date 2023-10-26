package com.facebook.facade;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.chat.ChatRequest;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.dto.chat.ChatSqlResult;
import com.facebook.dto.message.MessageResponseList;
import com.facebook.exception.NotFoundException;
import com.facebook.exception.UserNotFoundException;
import com.facebook.model.AppUser;
import com.facebook.model.chat.Chat;
import com.facebook.model.chat.Message;
import com.facebook.service.AppUserService;
import com.facebook.service.ChatService;
import com.facebook.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class ChatFacade {
    private final ModelMapper modelMapper;
//    private final AppUserFacade userFacade;
//    private final MessageFacade messageFacade;
//    private final MessageService messageService;
    private final ChatService chatService;
    private final AppUserService appUserService;
    public ChatResponse convertToChatResponse(Chat chat, AppUser chatP) {
        ChatResponse chatResponse = modelMapper.map(chat, ChatResponse.class);
        AppUserChatResponse appUserChatResponse = modelMapper.map(chatP, AppUserChatResponse.class);
        chatResponse.setChatParticipant(appUserChatResponse);
        return chatResponse;
    }
    public ChatResponseList convertToChatResponseList(Chat chat) {
        ChatResponseList chatResponse = modelMapper.map(chat, ChatResponseList.class);

        AppUser receiverUser = getReceiverUser(chat.getId());
        AppUserChatResponse appUserChatResponse = modelMapper.map(receiverUser, AppUserChatResponse.class);
        chatResponse.setChatParticipant(appUserChatResponse);

        getLastMessageInChat(chat).ifPresent(chatResponse::setLastMessage);

        return chatResponse;
    }

    private ChatSqlResult convertToChatSqlResult(Map<String, Object> sqlResult){
        return modelMapper.map(sqlResult, ChatSqlResult.class);
    }
    public ChatResponse convertToChatResponseFromSql(Map<String, Object> sqlResult){
        ChatSqlResult result = convertToChatSqlResult(sqlResult);
        AppUserChatResponse user = modelMapper.map(result, AppUserChatResponse.class);
        user.setId(result.getUserId());
        ChatResponse chat = modelMapper.map(result, ChatResponse.class);
        chat.setChatParticipant(user);
        return chat;
    }
    public ChatResponse createChat(String username){
        AppUser authUser = appUserService.getAuthUser();

        AppUser chatParticipant = appUserService.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        Chat chat = chatService.createNewChat(authUser, chatParticipant);
        return convertToChatResponse(chat, chatParticipant);
    }
    public List<ChatResponse> searchChats(String input, Pageable pageable){
        AppUser authUser = appUserService.getAuthUser();
        return chatService.searchChats(authUser, input, pageable)
                .stream()
                .map(this::convertToChatResponseFromSql)
                .toList();
    }
    public List<ChatResponseList> getAllUserChats(Pageable pageable){
        AppUser authUser = appUserService.getAuthUser();
        return chatService.getAllUserChats(authUser, pageable)
                .stream()
                .map(this::convertToChatResponseList)
                .toList();
    }
    private MessageResponseList convertToMessageResponseList(Message message) {
        AppUser sender = message.getSender();
        AppUserChatResponse appUserResponse = modelMapper.map(sender, AppUserChatResponse.class);
        MessageResponseList messageResponse = modelMapper.map(message, MessageResponseList.class);
        messageResponse.setSender(appUserResponse);
        messageResponse.setCreatedAt(message.getCreatedDate());
        return messageResponse;
    }
    private Optional<MessageResponseList> getLastMessageInChat(Chat chat){
        return chat.getMessages().stream()
                .sorted(Comparator.comparing(Message::getCreatedDate).reversed())
                .limit(1)
                .map(this::convertToMessageResponseList)
                .findFirst();
    }
    public ChatResponse getChatById(Long id){
        Chat chat = chatService.findChatById(id);
        AppUser authUser = appUserService.getAuthUser();
        if(!chat.getChatParticipants().contains(authUser)){
            throw new NotFoundException("Chat not found.");
        }
        return convertToChatResponse(chat, getReceiverUser(id));
    }
    public AppUser getReceiverUser(Long chatId){
        AppUser authUser = appUserService.getAuthUser();
        return chatService.getReceiverUser(authUser, chatId);
    }
    public AppUser getReceiverUser(Long chatId, Principal principal){
        AppUser authUser = appUserService.getAuthUser(principal);
        return chatService.getReceiverUser(authUser, chatId);
    }
}
