package com.facebook.dto.message;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.chat.ChatResponse;
import com.facebook.model.chat.ContentType;
import com.facebook.model.chat.MessageStatus;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MessageResponse {
    private Long id;
    private ContentType contentType;
    private String content;
    private AppUserChatResponse sender;
    private ChatResponse chat;
    private ZonedDateTime createdAt;
    private MessageStatus status;
}
