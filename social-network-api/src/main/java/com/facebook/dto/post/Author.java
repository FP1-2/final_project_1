package com.facebook.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO представлення автора.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    private Long userId;
    private String name;
    private String surname;
    private String username;
    private String avatar;

}
