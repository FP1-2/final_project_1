package com.facebook.dto.chat;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.message.MessageResponseList;
import lombok.Data;


@Data
public class ChatResponseList {
    private Long id;
    private AppUserChatResponse chatParticipant;
    private MessageResponseList lastMessage;
}
