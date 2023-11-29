package com.facebook.controller;

import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.friends.FriendsRequest;
import com.facebook.dto.friends.FriendsResponse;
import com.facebook.dto.friends.FriendsStatusRequest;
import com.facebook.dto.friends.FriendsStatusResponse;
import com.facebook.service.CurrentUserService;
import com.facebook.service.FriendsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = FriendsController.class)
class FriendsControllerTest {

    @Autowired
    private FriendsController friendsController;

    @MockBean
    private FriendsService friendsService;

    @MockBean
    private CurrentUserService currentUserService;

    private static final Long USER_ID_1 = 123L;

    private static final Long USER_ID_2 = 456L;

    @BeforeEach
    void setUp() {
        reset(friendsService, currentUserService);
    }

    @Test
    void sendFriendRequestTest() {
        FriendsRequest request = new FriendsRequest();
        request.setFriendId(123L);

        when(currentUserService.getCurrentUserId()).thenReturn(USER_ID_2);
        when(friendsService.sendFriendRequest(USER_ID_2, USER_ID_1)).thenReturn(new FriendsResponse());

        ResponseEntity<FriendsResponse> response = friendsController.sendFriendRequest(request);

        verify(currentUserService).getCurrentUserId();
        verify(friendsService).sendFriendRequest(USER_ID_2, USER_ID_1);
    }

    @Test
    void friendsStatusTest() {
        FriendsStatusRequest request = new FriendsStatusRequest();
        request.setUserId(USER_ID_1);
        request.setStatus(true);

        when(currentUserService.getCurrentUserId()).thenReturn(USER_ID_2);
        doNothing().when(friendsService).changeFriendsStatus(USER_ID_1, USER_ID_2, true);

        ResponseEntity<FriendsStatusResponse> response = friendsController.friendsStatus(request);

        verify(currentUserService).getCurrentUserId();
        verify(friendsService).changeFriendsStatus(USER_ID_1, USER_ID_2, true);
    }


    @Test
    void deleteFriendTest() {
        FriendsRequest request = new FriendsRequest();
        request.setFriendId(USER_ID_1);

        when(currentUserService.getCurrentUserId()).thenReturn(USER_ID_2);
        doNothing().when(friendsService).deleteFriend(USER_ID_2, USER_ID_1);

        ResponseEntity<FriendsResponse> response = friendsController.deleteFriend(request);

        verify(currentUserService).getCurrentUserId();
        verify(friendsService).deleteFriend(USER_ID_2, USER_ID_1);
    }

    @Test
    void getFriendsByAuthTest() {
        when(currentUserService.getCurrentUserId()).thenReturn(USER_ID_2);
        List<AppUserResponse> mockResponseList = List.of(new AppUserResponse(), new AppUserResponse());
        when(friendsService.getFriendsByUserId(USER_ID_2)).thenReturn(mockResponseList);

        ResponseEntity<List<AppUserResponse>> response = friendsController.getFriendsByAuth();

        verify(currentUserService).getCurrentUserId();
        verify(friendsService).getFriendsByUserId(USER_ID_2);
    }

    @Test
    void cancelFriendRequestTest() {
        FriendsRequest request = new FriendsRequest();
        request.setFriendId(USER_ID_1);

        when(currentUserService.getCurrentUserId()).thenReturn(USER_ID_2);
        doNothing().when(friendsService).cancelFriendRequest(USER_ID_2, USER_ID_1);

        ResponseEntity<FriendsResponse> response = friendsController.cancelFriendRequest(request);

        verify(currentUserService).getCurrentUserId();
        verify(friendsService).cancelFriendRequest(USER_ID_2, USER_ID_1);
    }

    @Test
    void searchFriendsTest() {
        String searchInput = "John";
        AppUserResponse u1 = new AppUserResponse();
        u1.setName("John Doe");
        AppUserResponse u2 = new AppUserResponse();
        u2.setName("Johnny Smith");
        List<AppUserResponse> list = List.of(u1, u2);

        when(friendsService.searchFriends(searchInput)).thenReturn(list);

        ResponseEntity<List<AppUserResponse>> responseEntity = friendsController.searchFriends(searchInput);

        assertEquals(200, responseEntity.getStatusCodeValue());
        List<AppUserResponse> actualSearchResults = responseEntity.getBody();
        assertEquals(list.size(), actualSearchResults.size());
    }

}
