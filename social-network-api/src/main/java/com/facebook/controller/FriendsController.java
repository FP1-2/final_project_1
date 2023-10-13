package com.facebook.controller;

import com.facebook.dto.friends.FriendsRequest;
import com.facebook.dto.friends.FriendsResponse;
import com.facebook.dto.friends.FriendsStatusRequest;
import com.facebook.facade.FriendsFacade;
import com.facebook.service.CurrentUserService;
import com.facebook.service.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    private final CurrentUserService currentUserService;

    private final FriendsFacade facade;

    @PostMapping("/send-request")
    public ResponseEntity<FriendsResponse> sendFriendRequest(@RequestBody FriendsRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        FriendsResponse response = facade.toFriendsResponse(friendsService.sendFriendRequest(userId, request.getFriendId()));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-status")
    public ResponseEntity<FriendsResponse> friendsStatus(@RequestBody FriendsStatusRequest request) {
        Long friendId = currentUserService.getCurrentUserId();
        friendsService.changeFriendsStatus(
                request.getUserId(),
                friendId,
                request.getStatus()
        );
        return ResponseEntity.ok().build();
//        FriendsResponse response = facade.toFriendsResponse(
//        );
//        friendsService.friendsStatus(userId, friendId, status)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
    }

    //TODO check updateStatus and create deleteFriend
}
