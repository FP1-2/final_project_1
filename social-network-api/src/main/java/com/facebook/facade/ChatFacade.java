package com.facebook.facade;

import com.facebook.dto.chat.ChatRequest;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.dto.chat.ChatResponseList;
import com.facebook.dto.message.MessageResponseList;
import com.facebook.model.Chat;
import com.facebook.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@RequiredArgsConstructor
public class ChatFacade {
    private final ModelMapper modelMapper;
    private final MessageFacade messageFacade;
    public ChatResponse convertToChatResponse(Chat chat) {
        return modelMapper.map(chat, ChatResponse.class);
    }
    public ChatResponseList convertToChatResponseList(Chat chat) {
        ChatResponseList chatR = modelMapper.map(chat, ChatResponseList.class);

        if(!chat.getMessages().isEmpty()) {
            List<MessageResponseList> list = chat.getMessages().stream()
                    .sorted(Comparator.comparing(Message::getCreatedDate).reversed())
                    .map(messageFacade::convertToMessageResponseList)
                    .limit(1).toList();
            chatR.setLastMessage(list.get(0));
        }
        chatR.setLastMessage(null);
        return chatR;
    }

    public Chat convertToChat(ChatRequest chatRequest) {
       return modelMapper.map(chatRequest, Chat.class);
    }
}
