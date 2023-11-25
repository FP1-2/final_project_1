package com.facebook.facade;

import com.facebook.dto.groups.GroupMembersDto;
import com.facebook.dto.groups.GroupPostRequest;
import com.facebook.dto.groups.UserGroup;
import com.facebook.model.AppUser;
import com.facebook.model.groups.Group;
import com.facebook.model.groups.GroupMembers;
import com.facebook.model.groups.GroupPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Фасад для перетворення об'єктів, пов'язаних з групами.
 * Використовує ModelMapper для спрощення процесу мапінгу між сутностями і DTO.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class GroupFacade {

    private final ModelMapper modelMapper;

    /**
     * Перетворює об'єкт GroupMembers в DTO.
     *
     * @param members Об'єкт GroupMembers, який необхідно перетворити.
     * @return GroupMembersDto - DTO, який представляє членство у групі.
     */
    public GroupMembersDto mapToGroupMembersDto(GroupMembers members) {
        GroupMembersDto dto = new GroupMembersDto();
        dto.setId(members.getId());
        dto.setUser(modelMapper.map(members.getUser(), UserGroup.class));
        dto.setRoles(members.getRoles());
        return dto;
    }

    public GroupPost convertGroupPostRequestToGroupPost(GroupPostRequest request, AppUser user, Group group) {
        GroupPost groupPost = new GroupPost();

        groupPost.setImageUrl(request.getImageUrl());
        groupPost.setTitle(request.getTitle());
        groupPost.setBody(request.getBody());
        groupPost.setType(request.getType());

        groupPost.setGroup(group);
        groupPost.setUser(user);

        return groupPost;
    }

}
