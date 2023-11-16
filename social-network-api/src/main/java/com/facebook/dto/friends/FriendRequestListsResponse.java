package com.facebook.dto.friends;

import com.facebook.dto.appuser.AppUserResponse;
import lombok.Data;

import java.util.List;

@Data
public class FriendRequestListsResponse {

    private List<AppUserResponse> send;

    private List<AppUserResponse> received;
}
