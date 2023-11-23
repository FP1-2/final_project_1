package com.facebook.facade;

import com.facebook.dto.groups.GroupMembershipDto;
import com.facebook.dto.groups.GroupResponse;
import com.facebook.dto.groups.UserGroup;
import com.facebook.model.groups.Group;
import com.facebook.model.groups.GroupMembers;
import com.facebook.model.groups.GroupRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Component
@RequiredArgsConstructor
public class GroupFacade {

    private final ModelMapper modelMapper;

    public GroupResponse mapToGroupResponse(Group group) {
        GroupResponse response = modelMapper.map(group, GroupResponse.class);

        Set<GroupMembershipDto> membersDto = Optional.ofNullable(group.getMembers())
                .orElse(Collections.emptySet())
                .stream()
                .map(this::mapToGroupMembershipDto)
                .collect(Collectors.toSet());

        Set<GroupMembershipDto> adminsDto = membersDto.stream()
                .filter(m -> Arrays.asList(m.getRoles()).contains(GroupRole.ADMIN))
                .collect(Collectors.toSet());

        response.setMembers(membersDto);
        response.setAdmins(adminsDto);

        return response;
    }

    public GroupMembershipDto mapToGroupMembershipDto(GroupMembers membership) {
        GroupMembershipDto dto = new GroupMembershipDto();
        dto.setId(membership.getId());
        dto.setUser(modelMapper.map(membership.getUser(), UserGroup.class));
        dto.setRoles(membership.getRoles());
        return dto;
    }

}
