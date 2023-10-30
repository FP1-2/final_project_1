package com.facebook.utils;

import org.springframework.data.domain.Sort;

/**
 * Утилітний клас, призначений для створення об'єктів сортування на основі вхідного рядка.
 * Допомагає перетворити рядок у форматі "властивість, напрямок" у об'єкт {@link Sort}.
 */
public class SortUtils {

    /**
     * Повертає об'єкт сортування на основі вхідного рядка сортування.
     *
     * @param sort рядок сортування у форматі "властивість, напрямок".
     *             Напрямок може бути або "asc", або "desc".
     *             Якщо напрямок відсутній,
     *             за замовчуванням вважається "asc".
     * @return об'єкт {@link Sort} для використання в запитах до репозиторію.
     */
    public static Sort getSorting(String sort) {
        String[] sortParts = sort.split(",");
        String property = sortParts[0];

        String direction = sortParts.length > 1 ? sortParts[1] : "asc";

        return direction.equalsIgnoreCase("desc")
                ? Sort.by(Sort.Order.desc(property))
                : Sort.by(Sort.Order.asc(property));
    }

}
