package com.facebook.model.Chat;

import com.facebook.model.chat.ContentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContentTypeTest {
    @Test
    public void testFromString() {
        assertEquals(ContentType.TEXT, ContentType.fromString("text"));
        assertEquals(ContentType.IMAGE, ContentType.fromString("image"));
        assertEquals(ContentType.LIKE, ContentType.fromString("like"));
    }

    @Test
    public void testFromStringWithInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> ContentType.fromString("invalid"));
    }

}
