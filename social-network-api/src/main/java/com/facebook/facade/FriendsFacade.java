package com.facebook.facade;

import com.facebook.dto.friends.*;
import com.facebook.model.friends.Friends;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendsFacade {

    private final ModelMapper modelMapper;

    public FriendsResponse toFriendsResponse(Friends f) {
        return modelMapper.map(f, FriendsResponse.class);
    }

}
