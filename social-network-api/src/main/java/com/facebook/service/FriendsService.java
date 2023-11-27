package com.facebook.service;

import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.friends.FriendRequestListsResponse;
import com.facebook.dto.friends.FriendsResponse;
import com.facebook.exception.AlreadyExistsException;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.AppUserFacade;
import com.facebook.facade.FriendsFacade;
import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.model.friends.FriendsStatus;
import com.facebook.model.notifications.NotificationType;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.FriendsRepository;
import com.facebook.repository.notifications.NotificationRepository;
import com.facebook.service.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;

    private final AppUserRepository appUserRepository;

    private final FriendsFacade facade;

    private final NotificationService notificationService;

    private final AppUserFacade userFacade;

    private final AppUserService appUserService;

    private final NotificationRepository notificationRepository;

    private static final String FRIENDS_NOT_FOUND_ERROR_MSG = "Friends pair not found";

    public FriendsResponse sendFriendRequest(Long userId, Long friendId) {
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
        friendsRepository.save(friendRequest);

        notificationService.createFriendRequestNotification(user, friend);
        return facade.toFriendsResponse(friendRequest);
    }

    public void cancelFriendRequest(Long userId, Long friendId) {
        Optional<Friends> existingRequest = friendsRepository.findFriendsByUserIdAndFriendIdAndStatus(userId, friendId);
        if(existingRequest.isPresent()) {
            friendsRepository.delete(existingRequest.get());
        } else {
            throw new NotFoundException("Friend request not found!");
        }
    }

    @Transactional
    public void changeFriendsStatus(Long userId, Long friendId, Boolean status) {
        notificationRepository
                .deleteByInitiatorAndUserAndType(friendId, userId, NotificationType.FRIEND_REQUEST);
        friendsRepository
                .findFriendsByUserIdAndFriendId(userId, friendId)
                .ifPresentOrElse(f -> {
                    if (Boolean.TRUE.equals(status)) {
                        f.setStatus(FriendsStatus.APPROVED);
                        friendsRepository.save(f);

                        if (!friendsRepository.existsByUserIdAndFriendId(friendId, userId)) {
                            friendsRepository.save(new Friends(f.getFriend(), f.getUser(), FriendsStatus.APPROVED));
                        }
                    }
                    else {
                        f.setStatus(FriendsStatus.REJECTED);
                        friendsRepository.delete(f);
                    }
                }, () -> {
                    log.warn("Friends pair not found for userId: {} and friendId: {}", userId, friendId);
                    throw new NotFoundException(FRIENDS_NOT_FOUND_ERROR_MSG);
                }
        );
    }

    public void deleteFriend(Long userId, Long friendId) {
        friendsRepository.findFriendsByUserIdAndFriendId(userId, friendId).ifPresentOrElse(
                friendsRepository::delete,
                () -> {
                    throw new NotFoundException(FRIENDS_NOT_FOUND_ERROR_MSG);
                }
        );
        friendsRepository.findFriendsByUserIdAndFriendId(friendId, userId).ifPresentOrElse(
                friendsRepository::delete,
                () -> {
                    throw new NotFoundException(FRIENDS_NOT_FOUND_ERROR_MSG);
                }
        );
    }

    public List<AppUserResponse> getFriendsByUserId(Long id) {
        return appUserRepository.findUserFriendsByUserId(id)
                .stream()
                .map(userFacade::convertToAppUserResponse)
                .toList();
    }

    public List<AppUserResponse> getFriendsRequest(Long userId) {
        return appUserRepository.findUserReceivedFriendsRequestsByUserId(userId)
                .stream()
                .map(userFacade::convertToAppUserResponse)
                .toList();
    }

    public FriendRequestListsResponse allFriendsRequests(Long userId) {
        List<AppUserResponse> received = appUserRepository.findUserReceivedFriendsRequestsByUserId(userId)
                .stream()
                .map(userFacade::convertToAppUserResponse)
                .toList();

        List<AppUserResponse> send = appUserRepository.findUserSendFriendsRequestsByUserId(userId)
                .stream()
                .map(userFacade::convertToAppUserResponse)
                .toList();

        FriendRequestListsResponse friendRequests = new FriendRequestListsResponse();
        friendRequests.setReceived(received);
        friendRequests.setSend(send);

        return friendRequests;
    }

    public List<AppUserResponse> searchFriends(String input) {
        AppUser authUser = appUserService.getAuthUser();

        return appUserRepository.searchFriendsByNameAndSurname(input, authUser.getId())
                .stream()
                .map(userFacade::convertToAppUserResponse)
                .toList();
    }

}
