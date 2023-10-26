package com.facebook.dto.message;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    @NotNull()
    private Long chatId;
    @NotNull()
    private String contentType;
    @NotNull()
    private String content;
}
