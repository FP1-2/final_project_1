package com.facebook.service;

import com.facebook.exception.AlreadyExistsException;
import com.facebook.exception.NotFoundException;
import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.model.friends.FriendsStatus;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.FriendsRepository;
import com.facebook.utils.EX;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;

    private final AppUserRepository appUserRepository;

    public Friends sendFriendRequest(Long userId, Long friendId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        AppUser friend = appUserRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found!"));
        Optional<Friends> duplicates = friendsRepository.findFriendsByUserIdAndFriendId(userId, friendId);
        if(duplicates.isPresent()) {
            throw new AlreadyExistsException("This pair of friends already exist");
        }
        Friends friendRequest = new Friends(user, friend, FriendsStatus.PENDING);
        return friendsRepository.save(friendRequest);
    }

    public Friends changeFriendsStatus(Long userId, Long friendId, FriendsStatus status) {
        //TODO maybe status should be boolean
        throw EX.NI;
    }

}
