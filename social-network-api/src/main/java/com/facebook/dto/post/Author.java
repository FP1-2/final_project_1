package com.facebook.dto.post;

import lombok.Data;

/**
 * DTO представлення автора.
 */
@Data
public class Author {

    private Long userId;

    private String name;

    private String surname;

    private String username;

    private String avatar;

}
