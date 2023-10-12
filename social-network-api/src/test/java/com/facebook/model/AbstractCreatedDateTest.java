package com.facebook.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тести для класу {@link AbstractCreatedDate}.
 */
class AbstractCreatedDateTest {

    /**
     * Тестує методи equals() та hashCode() класу {@link AbstractCreatedDate}.
     * Перевіряє рівність двох об'єктів з однаковими ID та датами створення.
     */
    @Test
    void testEqualsAndHashCodeWithSameCreatedDate() {
        AbstractCreatedDate entity1 = new TestEntity();
        entity1.setId(1L);
        entity1.setCreatedDate(LocalDateTime.now());

        AbstractCreatedDate entity2 = new TestEntity();
        entity2.setId(1L);
        entity2.setCreatedDate(entity1.getCreatedDate());

        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
    }

    /**
     * Тестовий клас, який розширює {@link AbstractCreatedDate},
     * створений для можливості інстанціювання.
     */
    private static class TestEntity extends AbstractCreatedDate { }

}

