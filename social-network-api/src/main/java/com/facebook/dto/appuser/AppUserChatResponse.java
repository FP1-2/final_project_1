package com.facebook.dto.appuser;

import lombok.Data;

import javax.swing.*;
import java.time.LocalDateTime;

@Data
public class AppUserChatResponse {
    private Long id;

    private String name;

    private String surname;

    private String username;

    private String avatar;
}
