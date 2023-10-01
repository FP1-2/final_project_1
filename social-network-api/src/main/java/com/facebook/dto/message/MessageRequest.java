package com.facebook.dto.message;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageRequest {
    @NotNull()
    private Long chatId;
    @NotNull()
    private String text;
}
