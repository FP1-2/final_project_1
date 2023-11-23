package com.facebook.facade;

import com.facebook.dto.groups.GroupMembershipDto;
import com.facebook.dto.groups.UserGroup;
import com.facebook.model.groups.GroupMembers;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class GroupFacade {

    private final ModelMapper modelMapper;

    public GroupMembershipDto mapToGroupMembershipDto(GroupMembers membership) {
        GroupMembershipDto dto = new GroupMembershipDto();
        dto.setId(membership.getId());
        dto.setUser(modelMapper.map(membership.getUser(), UserGroup.class));
        dto.setRoles(membership.getRoles());
        return dto;
    }

}
