package com.facebook.model.friends;

import com.facebook.model.AppUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FriendsTest {

    @Test
    void testFriendsConstructor() {
        Friends friends = new Friends();
        assertNotNull(friends);
    }

    @Test
    void testFriendsGettersAndSetters() {
        Friends friends = new Friends();

        friends.setStatus(FriendsStatus.PENDING);
        FriendsStatus status = friends.getStatus();
        assertEquals(FriendsStatus.PENDING, status);
    }

    @Test
    void testAppUserConstructor() {
        AppUser user = new AppUser();
        assertNotNull(user);
    }

    @Test
    void testUserGettersAndSetters() {
        AppUser user = new AppUser();

        user.setUsername("testUser");
        String username = user.getUsername();
        assertEquals("testUser", username);
    }

}
