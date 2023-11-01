package com.facebook.dto.chat;

import com.facebook.dto.appuser.AppUserChatResponse;
import lombok.Data;


@Data
public class ChatResponse {
    private Long id;
    private AppUserChatResponse chatParticipant;
}
