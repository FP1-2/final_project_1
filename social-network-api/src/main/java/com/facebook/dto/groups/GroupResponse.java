package com.facebook.dto.groups;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class GroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String imageUrl;
    private String name;
    private String description;
    private Set<GroupMembersDto> members;
    private Set<GroupMembersDto> admins;
}
