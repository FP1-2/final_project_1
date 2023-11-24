package com.facebook.service.groups;

import com.facebook.dto.groups.GroupResponse;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.GroupFacade;
import com.facebook.model.groups.Group;
import com.facebook.model.groups.GroupRole;
import com.facebook.repository.groups.GroupMembersRepository;
import com.facebook.repository.groups.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Допоміжний сервіс, який використовується для поділу логіки GroupService.
 * Це дозволяє уникнути прямих викликів транзакційних методів через 'this'
 * та вирішити проблему циклічних посилань у Spring-контейнері.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class GroupQueryService {

    private final GroupRepository groupRepository;

    private final GroupMembersRepository groupMembersRepository;

    private final GroupFacade groupFacade;

    private final ModelMapper modelMapper;

    /**
     * Отримує деталі групи разом з її адміністраторами та останніми членами.
     *
     * @param groupId Ідентифікатор групи, інформацію про яку потрібно отримати.
     * @return GroupResponse - DTO з інформацією про групу, її адміністраторів та останніх членів.
     * @throws NotFoundException якщо група з даним ідентифікатором не знайдена.
     */
    @Transactional
    public GroupResponse getGroupWithMembers(Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        GroupResponse groupResponse = modelMapper.map(group, GroupResponse.class);

        groupResponse
                .setAdmins(groupMembersRepository
                .findAdminsByGroupId(groupId, GroupRole.ADMIN)
                .stream()
                .map(groupFacade::mapToGroupMembersDto).collect(Collectors.toSet()));

        groupResponse
                .setMembers(groupMembersRepository
                .findLastMembersByGroupId(groupId, PageRequest.of(0, 10))
                .stream()
                .map(groupFacade::mapToGroupMembersDto).collect(Collectors.toSet()));

        return groupResponse;
    }

}
