package com.facebook.service;

import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.facade.AppUserFacade;
import com.facebook.facade.FriendsFacade;
import com.facebook.model.AppUser;
import com.facebook.model.friends.Friends;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.FriendsRepository;
import com.facebook.exception.NotFoundException;
import com.facebook.repository.notifications.NotificationRepository;
import com.facebook.service.notifications.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FriendsServiceTest {

    @Mock
    private FriendsRepository friendsRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private AppUserService appUserService;

    private FriendsService friendsService;

    private static final Long USER_ID_1 = 123L;

    private static final Long USER_ID_2 = 456L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ModelMapper modelMapper = new ModelMapper();

        FriendsFacade friendsFacade = new FriendsFacade(modelMapper);

        AppUserFacade userFacade = new AppUserFacade(modelMapper);


        friendsService = new FriendsService(friendsRepository,
                appUserRepository,
                friendsFacade,
                notificationService,
                userFacade,
                appUserService,
                notificationRepository);
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

    @Test
    void testCancelFriendRequest() {
        Friends friends = new Friends();
        when(friendsRepository.findFriendsByUserIdAndFriendIdAndStatus(USER_ID_1, USER_ID_2)).thenReturn(Optional.of(friends));

        friendsService.cancelFriendRequest(USER_ID_1, USER_ID_2);

        verify(friendsRepository).delete(friends);
    }

    @Test
    void testCancelFriendRequestNotFound() {
        when(friendsRepository.findFriendsByUserIdAndFriendIdAndStatus(USER_ID_1, USER_ID_2)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> friendsService.cancelFriendRequest(USER_ID_1, USER_ID_2));
    }

    @Test
    void testSearchFriends() {
        String searchInput = "John"; // Example search input
        AppUser authUser = new AppUser();
        authUser.setId(USER_ID_1); // Example authenticated user ID

        AppUser u1 = new AppUser();
        u1.setName("John Doe");
        AppUser u2 = new AppUser();
        u2.setName("Johnny Smith");
        List<AppUser> list = List.of(u1, u2);

        when(appUserService.getAuthUser()).thenReturn(authUser);

        when(appUserRepository.searchFriendsByNameAndSurname(eq(searchInput), eq(authUser.getId())))
                .thenReturn(list);

        List<AppUserResponse> actualSearchResults = friendsService.searchFriends(searchInput);

        assertEquals(2, actualSearchResults.size());
    }

}
