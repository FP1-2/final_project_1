package com.facebook.service.groups;

import com.facebook.dto.groups.GroupResponse;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.GroupFacade;
import com.facebook.model.groups.Group;
import com.facebook.model.groups.GroupMembers;
import com.facebook.model.groups.GroupRole;
import com.facebook.repository.groups.GroupMembersRepository;
import com.facebook.repository.groups.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class GroupQueryService {

    private final GroupRepository groupRepository;

    private final GroupMembersRepository groupMembersRepository;

    private final GroupFacade groupFacade;

    @Transactional
    public GroupResponse getGroupWithMembers(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        List<GroupMembers> admins = groupMembersRepository
                .findAdminsByGroupId(groupId, GroupRole.ADMIN);
        List<GroupMembers> lastMembers = groupMembersRepository
                .findLastMembersByGroupId(groupId, PageRequest.of(0, 10));

        GroupResponse groupResponse = groupFacade.mapToGroupResponse(group);
        groupResponse.setAdmins(admins
                .stream()
                .map(groupFacade::mapToGroupMembershipDto).collect(Collectors.toSet()));
        groupResponse.setMembers(lastMembers
                .stream()
                .map(groupFacade::mapToGroupMembershipDto).collect(Collectors.toSet()));
        return groupResponse;
    }

}
