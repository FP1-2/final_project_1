package com.facebook.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Тести для класу {@link AbstractId}.
 */
class AbstractIdTest {

    /**
     * Тестує методи equals() та hashCode() класу {@link AbstractId}.
     * Перевіряє рівність двох об'єктів з однаковими ID.
     */
    @Test
    void testEqualsAndHashCodeWithSameId() {
        AbstractId entity1 = new TestEntity();
        entity1.setId(1L);

        AbstractId entity2 = new TestEntity();
        entity2.setId(1L);

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    /**
     * Тестує методи equals() та hashCode() класу {@link AbstractId}.
     * Перевіряє, що об'єкти з різними ID не є рівними.
     */
    @Test
    void testEqualsAndHashCodeWithDifferentId() {
        AbstractId entity1 = new TestEntity();
        entity1.setId(1L);

        AbstractId entity2 = new TestEntity();
        entity2.setId(2L);

        assertNotEquals(entity1, entity2);
        assertNotEquals(entity1.hashCode(), entity2.hashCode());
    }

    /**
     * Тестовий клас, який розширює {@link AbstractId},
     * створений для можливості інстанціювання.
     */
    private static class TestEntity extends AbstractId { }
}

