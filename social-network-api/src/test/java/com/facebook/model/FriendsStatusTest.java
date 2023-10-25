package com.facebook.model;

import com.facebook.model.friends.FriendsStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FriendsStatusTest {

    @Test
    public void testEnumValues() {
        assertEquals("PENDING", FriendsStatus.PENDING.name());
        assertEquals("APPROVED", FriendsStatus.APPROVED.name());
        assertEquals("REJECTED", FriendsStatus.REJECTED.name());
    }

    @Test
    public void testEnumComparison() {
        FriendsStatus pending = FriendsStatus.PENDING;
        FriendsStatus approved = FriendsStatus.APPROVED;
        FriendsStatus rejected = FriendsStatus.REJECTED;

        assertEquals(pending, FriendsStatus.PENDING);
        assertEquals(approved, FriendsStatus.APPROVED);
        assertEquals(rejected, FriendsStatus.REJECTED);

        assertNotEquals(pending, approved);
        assertNotEquals(approved, rejected);
        assertNotEquals(rejected, pending);
    }

}
