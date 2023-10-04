package com.facebook.dto.chat;

import com.facebook.dto.appuser.AppUserChatResponse;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ChatResponse {
    private Long id;
    private List<AppUserChatResponse> chatParticipant;
}
