package com.facebook.service.groups;

import com.facebook.dto.groups.GroupMembersDto;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
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
    private final String GROUP_NOT_FOUND = "Group not found with id: ";

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
                .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND + groupId));

        GroupResponse groupResponse = modelMapper.map(group, GroupResponse.class);

        groupResponse
                .setAdmins(getGroupMembersByRole(groupId, GroupRole.ADMIN));
        groupResponse
                .setMembers(getGroupMembersByRole(groupId, GroupRole.MEMBER));

        return groupResponse;
    }

    /**
     * Отримує членів групи за їх роллю.
     * Використовує універсальний запит для фільтрації членів групи на основі конкретної ролі.
     * Дозволяє отримати, наприклад, лише адміністраторів, звичайних членів або заблокованих користувачів групи,
     * з врахуванням пагінації та сортування.
     *
     * @param groupId Ідентифікатор групи, члени якої будуть запитані.
     * @param role Роль членів групи (ADMIN, MEMBER, BANNED), яких необхідно отримати.
     * @return Набір DTO (GroupMembersDto) членів групи, відфільтрованих за заданою роллю.
     */
    private Set<GroupMembersDto> getGroupMembersByRole(Long groupId,
                                                       GroupRole role) {
     return groupMembersRepository
                .findMembersByGroupIdAndRoles(groupId,
                        role == GroupRole.MEMBER ? "MEMBER" : "",
                        role == GroupRole.ADMIN ? "ADMIN" : "",
                        role == GroupRole.BANNED ? "BANNED" : "",
                        PageRequest.of(0, 10,
                                Sort.Direction.DESC, "createdDate"))
                .stream()
                .map(groupFacade::mapToGroupMembersDto)
                .collect(Collectors.toSet());
    }

}
