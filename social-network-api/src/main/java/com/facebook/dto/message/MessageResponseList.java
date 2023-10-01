package com.facebook.dto.message;

import com.facebook.dto.appuser.AppUserChatResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseList {
    private Long id;
    private String text;
    private AppUserChatResponse sender;
    private LocalDateTime createdAt;
}
