package com.facebook.service.groups;

import com.facebook.dto.groups.GroupRequest;
import com.facebook.dto.groups.GroupResponse;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.GroupFacade;
import com.facebook.model.AppUser;
import com.facebook.model.groups.Group;
import com.facebook.model.groups.GroupMembers;
import com.facebook.model.groups.GroupRole;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.groups.GroupMembersRepository;
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

    private final GroupMembersRepository groupMembersRepository;

    private final GroupPostRepository groupPostRepository;

    private final GroupFacade groupFacade;

    private final ModelMapper modelMapper;

    private final AppUserRepository appUserRepository;

    private final GroupQueryService groupQueryService;

    @Transactional
    public GroupResponse createGroup(GroupRequest groupRequest, Long userId) {
        Group group = modelMapper.map(groupRequest, Group.class);
        Group savedGroup = groupRepository.save(group);

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        GroupMembers membership = new GroupMembers();
        membership.setUser(user);
        membership.setGroup(savedGroup);
        membership.setRoles(new GroupRole[]{GroupRole.ADMIN, GroupRole.MEMBER});
        groupMembersRepository.save(membership);

        return groupQueryService.getGroupWithMembers(savedGroup.getId());
    }

}


