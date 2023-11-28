package com.facebook.dto.groups;

import com.facebook.model.groups.GroupRole;
import lombok.Data;

/**
 * DTO, який представляє члена групи.
 */
@Data
public class GroupMember {
    private Long id;
    private UserGroup user;
    private GroupRole[] roles;
}
