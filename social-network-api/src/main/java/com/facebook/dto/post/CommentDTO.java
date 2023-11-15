package com.facebook.dto.post;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для представлення коментаря.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;

    private String content;

    private LocalDateTime createdDate;

    private Author appUser;

}
