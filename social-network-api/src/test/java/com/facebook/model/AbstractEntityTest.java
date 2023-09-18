package com.facebook.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractEntityTest {
    @Test
    void testEqualsAndHashCode() {
        AbstractEntity entity1 = new AbstractEntity() {};
        entity1.setId(1L);
        entity1.setCreatedDate(LocalDateTime.now());
        entity1.setLastModifiedDate(LocalDateTime.now());

        AbstractEntity entity2 = new AbstractEntity() {};
        entity2.setId(1L);
        entity2.setCreatedDate(entity1.getCreatedDate());
        entity2.setLastModifiedDate(entity1.getLastModifiedDate());

        AbstractEntity entity3 = new AbstractEntity() {};
        entity3.setId(2L);
        entity3.setCreatedDate(LocalDateTime.now().minusDays(1));
        entity3.setLastModifiedDate(LocalDateTime.now().minusDays(1));

        // Перевірка equals
        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);

        // Перевірка hashCode
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }

    @Test
    void testEntityIsNotEqualToNullOrOtherTypes() {
        AbstractEntity entity = new AbstractEntity() {};
        entity.setId(1L);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setLastModifiedDate(LocalDateTime.now());

        assertNotEquals(null, entity);
        assertNotEquals("string", entity);
    }
}
