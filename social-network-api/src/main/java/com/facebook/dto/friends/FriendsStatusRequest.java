package com.facebook.dto.friends;

import com.facebook.model.friends.FriendsStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendsStatusRequest {

    @NotNull
    private Long userId;

    @NotNull
    private FriendsStatus status;
}
