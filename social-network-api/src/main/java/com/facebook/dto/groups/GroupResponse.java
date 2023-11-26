package com.facebook.dto.groups;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO (Data Transfer Object) клас для представлення інформації про групу в соціальній мережі.
 * Включає інформацію про ідентифікатор групи, дату створення, дату останньої модифікації,
 * URL зображення групи, назву, опис, а також набори членів та адміністраторів групи.
 */
@Data
public class GroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String imageUrl;
    private String name;
    private String description;
    private Set<GroupMember> members;
    private Set<GroupMember> admins;
}
