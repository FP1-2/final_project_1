package com.facebook.dto.chat;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatSqlResult {
    Long id;
    Timestamp created_date;
    Timestamp last_modified_date;
    Long userId;
    String name;
    String username;
    String surname;
    String avatar;
}
