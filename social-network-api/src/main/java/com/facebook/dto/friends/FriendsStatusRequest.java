package com.facebook.dto.friends;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendsStatusRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Boolean status;
}
