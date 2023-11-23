package com.facebook.service.groups;

import com.facebook.dto.groups.GroupRequest;
import com.facebook.dto.groups.GroupResponse;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.GroupFacade;
import com.facebook.model.AppUser;
import com.facebook.model.groups.Group;
import com.facebook.model.groups.GroupMembership;
import com.facebook.model.groups.GroupRole;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.groups.GroupMembershipRepository;
import com.facebook.repository.groups.GroupPostRepository;
import com.facebook.repository.groups.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final GroupMembershipRepository groupMembershipRepository;

    private final GroupPostRepository groupPostRepository;

    private final GroupFacade groupFacade;

    private final ModelMapper modelMapper;

    private final AppUserRepository appUserRepository;

    @Transactional
    public GroupResponse createGroup(GroupRequest groupRequest, Long userId) {
        Group group = modelMapper.map(groupRequest, Group.class);
        Group savedGroup = groupRepository.save(group);

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        GroupMembership membership = new GroupMembership();
        membership.setUser(user);
        membership.setGroup(savedGroup);
        membership.setRoles(new GroupRole[]{GroupRole.ADMIN, GroupRole.MEMBER});
        groupMembershipRepository.save(membership);

        return getGroupWithMembers(savedGroup.getId());
    }

    @Transactional
    public GroupResponse getGroupWithMembers(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        List<GroupMembership> admins = groupMembershipRepository
                .findAdminsByGroupId(groupId, GroupRole.ADMIN);
        List<GroupMembership> lastMembers = groupMembershipRepository
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


