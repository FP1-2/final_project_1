package com.facebook.model.Chat;

import com.facebook.model.chat.MessageStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageStatusTest {
    @Test
    void testFromString() {
        assertEquals(MessageStatus.SENT, MessageStatus.fromString("sent"));
        assertEquals(MessageStatus.READ, MessageStatus.fromString("read"));
        assertEquals(MessageStatus.FAILED, MessageStatus.fromString("failed"));
    }

    @Test
    void testFromStringWithInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> MessageStatus.fromString("invalid"));
    }
}

