package com.facebook.dto.friends;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class FriendsStatusResponse {

    private final Long potentialFriendId;

    private final String message;

    @JsonIgnore
    private final boolean status;

    private FriendsStatusResponse(Long potentialFriendId, boolean status) {
        this.potentialFriendId = potentialFriendId;
        this.status = status;
        this.message = createMessage();
    }

    public static FriendsStatusResponse of(Long potentialFriendId, boolean status) {
        return new FriendsStatusResponse(potentialFriendId, status);
    }

    String createMessage() {
        return this.status ? "You have successfully accepted the request."
                : "You have deleted the friend request.";
    }

}
