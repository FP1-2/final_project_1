package com.facebook.facade;

import com.facebook.dto.groups.GroupMembersDto;
import com.facebook.dto.groups.UserGroup;
import com.facebook.model.groups.GroupMembers;
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

}
