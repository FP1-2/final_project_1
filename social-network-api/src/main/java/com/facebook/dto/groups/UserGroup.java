package com.facebook.dto.groups;

import lombok.Data;

@Data
public class UserGroup {
    private Long userId;
    private String name;
    private String surname;
    private String username;
    private String avatar;
}
