package com.facebook.service;

import com.facebook.dto.friends.FriendsResponse;
import com.facebook.exception.AlreadyExistsException;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.FriendsFacade;
import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.model.friends.FriendsStatus;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;

    private final AppUserRepository appUserRepository;

    private final FriendsFacade facade;

    public Friends sendFriendRequest(Long userId, Long friendId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        AppUser friend = appUserRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found!"));

        Optional<Friends> duplicates = friendsRepository.findFriendsByUserIdAndFriendId(userId, friendId);
        if(duplicates.isPresent()) {
            throw new AlreadyExistsException("Friend request already sent!");
        }

        Optional<Friends> sentRequest = friendsRepository.findFriendsByUserIdAndFriendId(friendId, userId);
        if(sentRequest.isPresent()) {
            throw new AlreadyExistsException("Friend already sent you the request. Check your friend requests!");
        }

        Friends friendRequest = new Friends(user, friend, FriendsStatus.PENDING);
        return friendsRepository.save(friendRequest);
    }

    public void changeFriendsStatus(Long userId, Long friendId, Boolean status) {
        friendsRepository.findFriendsByUserIdAndFriendId(userId, friendId).ifPresentOrElse(
                (f) -> {
                    if (status) {
                        f.setStatus(FriendsStatus.APPROVED);
                        friendsRepository.save(f);

                        friendsRepository.save(new Friends(f.getFriend(), f.getUser(),FriendsStatus.APPROVED));
                    }
                    else {
                        f.setStatus(FriendsStatus.REJECTED);
                        friendsRepository.save(f);
                    }
                },
                () -> {
                    throw new NotFoundException("Friends pair not found");
                });
    }

    public void deleteFriend(Long userId, Long friendId) {
        friendsRepository.findFriendsByUserIdAndFriendId(userId, friendId).ifPresentOrElse(
                friendsRepository::delete,
                () -> {
                    throw new NotFoundException("Friends pair not found");
                }
        );
        friendsRepository.findFriendsByUserIdAndFriendId(friendId, userId).ifPresentOrElse(
                friendsRepository::delete,
                () -> {
                    throw new NotFoundException("Friends pair not found");
                }
        );
    }

    public List<FriendsResponse> getFriendsByUserId(Long id) {
        return friendsRepository.findFriendsByUserId(id)
                .stream()
                .map(facade::toFriendsResponse)
                .collect(Collectors.toList());
    }

}
