package com.facebook.dto.friends;

import com.facebook.model.friends.FriendsStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendsResponse {

    private Long id;

    private LocalDateTime created_date;

    private LocalDateTime last_modified_date;

    private Long userId;

    private Long friendId;

    private FriendsStatus status;
}
