package com.facebook.dto.friends;

import com.facebook.model.friends.Friends;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppUserFriends {

    private Long id;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private String name;

    private String surname;

    private String username;

    private String address;

    private String avatar;

    private Friends friends;
}

