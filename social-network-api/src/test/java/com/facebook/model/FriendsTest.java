package com.facebook.model;

import com.facebook.model.friends.Friends;
import com.facebook.model.friends.FriendsStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendsTest {
    @Test
    public void testFriendsConstructor() {
        Friends friends = new Friends();
        assertNotNull(friends);
    }

    @Test
    public void testFriendsGettersAndSetters() {
        Friends friends = new Friends();

        friends.setStatus(FriendsStatus.PENDING);
        FriendsStatus status = friends.getStatus();
        assertEquals(FriendsStatus.PENDING, status);
    }

    @Test
    public void testAppUserConstructor() {
        AppUser user = new AppUser();
        assertNotNull(user);
    }

    @Test
    public void testUserGettersAndSetters() {
        AppUser user = new AppUser();

        user.setUsername("testUser");
        String username = user.getUsername();
        assertEquals("testUser", username);
    }

}
