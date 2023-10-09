package com.facebook.controller.posts;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Об'єкт відповіді для сторінкової видачі коментарів.
 *
 * @param <T> Тип даних, які містяться
 *           у відповіді (наприклад, CommentDTO).
 */
@Data
@NoArgsConstructor
public class PageDtoForComment<T> {
    /** Список даних на поточній сторінці. */
    private List<T> content;
    /** Загальна кількість елементів. */
    private long totalElements;
    /** Загальна кількість сторінок. */
    private int totalPages;
    /** Номер поточної сторінки (починаючи з 0). */
    private int number;
    /** Розмір сторінки (кількість елементів на сторінці). */
    private int size;
}

