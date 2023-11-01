package com.facebook.service;

import com.facebook.facade.AppUserFacade;
import com.facebook.facade.FriendsFacade;
import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.FriendsRepository;
import com.facebook.exception.NotFoundException;
import com.facebook.service.notifications.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FriendsServiceTest {

    @Mock
    private FriendsRepository friendsRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private NotificationService notificationService;

    private FriendsService friendsService;

    private static final Long USER_ID_1 = 123L;

    private static final Long USER_ID_2 = 456L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ModelMapper modelMapper = new ModelMapper();

        FriendsFacade friendsFacade = new FriendsFacade(modelMapper);

        AppUserFacade userFacade = new AppUserFacade(modelMapper);

        friendsService = new FriendsService(friendsRepository, appUserRepository, friendsFacade, notificationService, userFacade);
    }

    @Test
    void testSendFriendRequest() {
        AppUser user = new AppUser();
        user.setId(USER_ID_2);

        AppUser friend = new AppUser();
        friend.setId(USER_ID_1);

        when(appUserRepository.findById(USER_ID_2)).thenReturn(Optional.of(user));
        when(appUserRepository.findById(USER_ID_1)).thenReturn(Optional.of(friend));
        when(friendsRepository.findFriendsByUserIdAndFriendId(USER_ID_1, USER_ID_2)).thenReturn(Optional.empty());
        when(friendsRepository.findFriendsByUserIdAndFriendId(USER_ID_2, USER_ID_1)).thenReturn(Optional.empty());

        friendsService.sendFriendRequest(USER_ID_1, USER_ID_2);

        verify(friendsRepository).save(any(Friends.class));
    }

    @Test
    void testSendFriendRequestAlreadyExists() {
        when(friendsRepository.findFriendsByUserIdAndFriendId(USER_ID_1, USER_ID_2)).thenReturn(Optional.of(new Friends()));

        assertThrows(NotFoundException.class,
                () -> friendsService.sendFriendRequest(USER_ID_1, USER_ID_2));
    }

    @Test
    void testChangeFriendsStatus1() {
        Friends friends = new Friends();
        when(friendsRepository.findFriendsByUserIdAndFriendId(USER_ID_1, USER_ID_2)).thenReturn(Optional.of(friends));

        assertThrows(NotFoundException.class,
                () -> friendsService.changeFriendsStatus(USER_ID_2, USER_ID_1, true));
    }

    @Test
    void testChangeFriendsStatusNotFound() {
        when(friendsRepository.findFriendsByUserIdAndFriendId(USER_ID_1, USER_ID_2)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> friendsService.changeFriendsStatus(USER_ID_2, USER_ID_1, true));
    }

    @Test
    void testDeleteFriend() {
        Friends friends = new Friends();
        when(friendsRepository.findFriendsByUserIdAndFriendId(USER_ID_1, USER_ID_2)).thenReturn(Optional.of(friends));
        when(friendsRepository.findFriendsByUserIdAndFriendId(USER_ID_2, USER_ID_1)).thenReturn(Optional.of(friends));

        friendsService.deleteFriend(USER_ID_2, USER_ID_1);

        verify(friendsRepository, times(2)).delete(any(Friends.class));
    }

    @Test
    void testDeleteFriendNotFound() {
        when(friendsRepository.findFriendsByUserIdAndFriendId(USER_ID_1, USER_ID_2)).thenReturn(Optional.empty());
        when(friendsRepository.findFriendsByUserIdAndFriendId(USER_ID_2, USER_ID_1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> friendsService.deleteFriend(USER_ID_2, USER_ID_1));
    }

}
