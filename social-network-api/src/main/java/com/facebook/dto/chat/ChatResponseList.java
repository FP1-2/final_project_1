package com.facebook.dto.chat;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.message.MessageResponseList;
import lombok.Data;

import java.util.List;

@Data
public class ChatResponseList {
    private Long id;
    private List<AppUserChatResponse> chatParticipants;
    private MessageResponseList lastMessage;
}
