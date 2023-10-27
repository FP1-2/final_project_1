package com.facebook.dto.friends;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendsRequest {

    @NotNull
    private Long friendId;
}
