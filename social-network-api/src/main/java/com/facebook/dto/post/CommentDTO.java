package com.facebook.dto.post;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * DTO для представлення коментаря.
 */
@Data
public class CommentDTO {

    private Long id;

    private String content;

    private LocalDateTime createdDate;

    private Author appUser;

}
