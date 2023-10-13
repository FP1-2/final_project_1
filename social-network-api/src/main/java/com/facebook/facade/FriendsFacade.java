package com.facebook.facade;

import com.facebook.dto.appuser.AppUserEditRequest;
import com.facebook.dto.friends.*;
import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FriendsFacade {

    private final ModelMapper modelMapper;

    public Friends toFriends(FriendsRequest request) {
        return modelMapper.map(request, Friends.class);
    }

    public Friends toFriends(FriendsStatusRequest statusRequest) {
        return modelMapper.map(statusRequest, Friends.class);
    }

    public FriendsResponse toFriendsResponse(Friends f) {
        FriendsResponse response = modelMapper.map(f, FriendsResponse.class);
        response.setCreated_date(f.getCreatedDate());
        response.setLast_modified_date(f.getLastModifiedDate());
        return response;
    }

}
