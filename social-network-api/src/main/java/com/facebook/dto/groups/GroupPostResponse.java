package com.facebook.dto.groups;

import com.facebook.model.groups.PostStatus;
import com.facebook.model.posts.PostType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Клас DTO для представлення детальної інформації про пости у групах.
 * Цей клас містить інформацію з базового запиту, отриманого при мапингу з
 * (GroupPostBase). Також мапиться 'author' до (GroupMember)
 * додатковим запитом для отримання інформації про участника групи.
 * Містить інформацію про оригінальний пост, у випадку якщо
 * поточний пост є репостом.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupPostResponse {
    private Long id;
    private Long groupId;
    private PostStatus status;
    private GroupMember author;
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
    private GroupPostResponse originalPost;

}