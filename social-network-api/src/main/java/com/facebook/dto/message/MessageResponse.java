package com.facebook.dto.message;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.model.AppUser;
import com.facebook.model.Chat;
import com.facebook.model.MessageStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private Long id;
    private String text;
    private AppUserChatResponse sender;
    private ChatResponse chat;
    private LocalDateTime createdAt;
    private MessageStatus status;
}
