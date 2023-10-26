package com.facebook.controller;

import com.facebook.dto.friends.FriendsRequest;
import com.facebook.dto.friends.FriendsResponse;
import com.facebook.dto.friends.FriendsStatusRequest;
import com.facebook.service.CurrentUserService;
import com.facebook.service.FriendsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    private final CurrentUserService currentUserService;

    @PostMapping("/send-request")
    public ResponseEntity<FriendsResponse> sendFriendRequest(@Valid @RequestBody FriendsRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        return ResponseEntity.ok(friendsService.sendFriendRequest(userId, request.getFriendId()));
    }

    @PutMapping("/update-status")
    public ResponseEntity<FriendsResponse> friendsStatus(@Valid @RequestBody FriendsStatusRequest request) {
        Long friendId = currentUserService.getCurrentUserId();
        friendsService.changeFriendsStatus(
                request.getUserId(),
                friendId,
                request.getStatus()
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<FriendsResponse> deleteFriend(@Valid @RequestBody FriendsRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        friendsService.deleteFriend(
                userId,
                request.getFriendId()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<FriendsResponse>> getFriendsByAuth() {
        Long userId = currentUserService.getCurrentUserId();
        return ResponseEntity.ok(friendsService.getFriendsByUserId(userId));
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<FriendsResponse>> getFriendsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(friendsService.getFriendsByUserId(id));
    }

    @GetMapping("/list/friend-requests")
    public ResponseEntity<List<FriendsResponse>> getFriendsRequests() {
        Long userId = currentUserService.getCurrentUserId();
        return ResponseEntity.ok(friendsService.getFriendsRequest(userId));
    }

}
