package com.facebook.dto.groups;

import com.facebook.dto.post.Author;
import com.facebook.model.groups.PostStatus;
import com.facebook.model.posts.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Клас DTO для базової інформації про пости у групах.
 * Використовується для представлення даних, отриманих з запитів до бази даних.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPostBase {
    private Long id;
    private Long groupId;
    private PostStatus status;
    private Author author;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String imageUrl;
    private String title;
    private String body;
    private PostType type;
    private Boolean isFavorite;
    private Long comments;
    private Long likes;
    private Long reposts;
    private Long originalPostId;
}
