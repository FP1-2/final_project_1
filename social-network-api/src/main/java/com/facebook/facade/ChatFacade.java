//package com.facebook.facade;
//
//import com.facebook.dto.chat.ChatRequest;
//import com.facebook.dto.chat.ChatResponse;
//import com.facebook.model.Chat;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ChatFacade {
//    private final ModelMapper modelMapper;
//    public ChatResponse convertToChatResponse(Chat chat) {
//        return modelMapper.map(chat, ChatResponse.class);
//    }
//
//
//    public Chat convertToChat(ChatRequest chatRequest) {
//       return modelMapper.map(chatRequest, Chat.class);
//
//    }
//}
