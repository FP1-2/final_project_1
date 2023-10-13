package com.facebook.service;

import com.facebook.exception.AlreadyExistsException;
import com.facebook.exception.NotFoundException;
import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.model.friends.FriendsStatus;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.FriendsRepository;
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
            throw new AlreadyExistsException("Friend request already sent");
        }
        Friends friendRequest = new Friends(user, friend, FriendsStatus.PENDING);
        return friendsRepository.save(friendRequest);
    }

    public void changeFriendsStatus(Long userId, Long friendId, Boolean status) {
        friendsRepository.findFriendsByUserIdAndFriendId(userId, friendId).ifPresentOrElse(
                (f) -> {
                    if (status) {
                        f.setStatus(FriendsStatus.APPROVED);
                        friendsRepository.save(new Friends(f.getFriend(), f.getUser(),FriendsStatus.APPROVED));
                    }
                    else {
                        f.setStatus(FriendsStatus.REJECTED);
                    }
                },
                () -> {
                    throw new NotFoundException("Friends pair not found");
                });
    }

}
