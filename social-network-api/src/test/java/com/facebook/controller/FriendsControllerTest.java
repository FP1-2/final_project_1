package com.facebook.controller;

import com.facebook.dto.friends.FriendsRequest;
import com.facebook.dto.friends.FriendsResponse;
import com.facebook.dto.friends.FriendsStatusRequest;
import com.facebook.service.CurrentUserService;
import com.facebook.service.FriendsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = FriendsController.class)
public class FriendsControllerTest {

    @Autowired
    private FriendsController friendsController;

    @MockBean
    private FriendsService friendsService;

    @MockBean
    private CurrentUserService currentUserService;

    @BeforeEach
    void setUp() {
        reset(friendsService, currentUserService);
    }

    @Test
    void sendFriendRequestTest() {
        FriendsRequest request = new FriendsRequest();
        request.setFriendId(123L);

        when(currentUserService.getCurrentUserId()).thenReturn(456L);
        when(friendsService.sendFriendRequest(456L, 123L)).thenReturn(new FriendsResponse());

        ResponseEntity<FriendsResponse> response = friendsController.sendFriendRequest(request);

        verify(currentUserService).getCurrentUserId();
        verify(friendsService).sendFriendRequest(456L, 123L);
    }

    @Test
    void friendsStatusTest() {
        FriendsStatusRequest request = new FriendsStatusRequest();
        request.setUserId(123L);
        request.setStatus(true);

        when(currentUserService.getCurrentUserId()).thenReturn(456L);
        doNothing().when(friendsService).changeFriendsStatus(123L, 456L, true);

        ResponseEntity<FriendsResponse> response = friendsController.friendsStatus(request);

        verify(currentUserService).getCurrentUserId();
        verify(friendsService).changeFriendsStatus(123L, 456L, true);
    }


    @Test
    void deleteFriendTest() {
        FriendsRequest request = new FriendsRequest();
        request.setFriendId(123L);

        when(currentUserService.getCurrentUserId()).thenReturn(456L);
        doNothing().when(friendsService).deleteFriend(456L, 123L);

        ResponseEntity<FriendsResponse> response = friendsController.deleteFriend(request);

        verify(currentUserService).getCurrentUserId();
        verify(friendsService).deleteFriend(456L, 123L);
    }

    @Test
    void getFriendsByUserIdTest() {
        when(currentUserService.getCurrentUserId()).thenReturn(456L);
        List<FriendsResponse> mockResponseList = List.of(new FriendsResponse(), new FriendsResponse());
        when(friendsService.getFriendsByUserId(456L)).thenReturn(mockResponseList);

        ResponseEntity<List<FriendsResponse>> response = friendsController.getFriendsByUserId();

        verify(currentUserService).getCurrentUserId();
        verify(friendsService).getFriendsByUserId(456L);
    }

}
