package com.facebook.dto.groups;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupJpql {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String imageUrl;
    private String name;
    private String description;
    private Long memberCount;
    private Boolean isPublic;
}
