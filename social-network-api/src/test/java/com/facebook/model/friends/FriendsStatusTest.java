package com.facebook.model.friends;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FriendsStatusTest {

    @Test
    void testEnumValues() {
        assertEquals("PENDING", FriendsStatus.PENDING.name());
        assertEquals("APPROVED", FriendsStatus.APPROVED.name());
        assertEquals("REJECTED", FriendsStatus.REJECTED.name());
    }

    @Test
    void testEnumComparison() {
        FriendsStatus pending = FriendsStatus.PENDING;
        FriendsStatus approved = FriendsStatus.APPROVED;
        FriendsStatus rejected = FriendsStatus.REJECTED;

        assertEquals(FriendsStatus.PENDING, pending);
        assertEquals(FriendsStatus.APPROVED, approved);
        assertEquals(FriendsStatus.REJECTED, rejected);

        assertNotEquals(pending, approved);
        assertNotEquals(approved, rejected);
        assertNotEquals(rejected, pending);
    }

}
