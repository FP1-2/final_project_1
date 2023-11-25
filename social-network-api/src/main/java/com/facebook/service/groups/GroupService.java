package com.facebook.service.groups;

import com.facebook.dto.groups.GroupMembersDto;
import com.facebook.dto.groups.GroupPostBase;
import com.facebook.dto.groups.GroupPostRequest;
import com.facebook.dto.groups.GroupPostResponse;
import com.facebook.dto.groups.GroupRequest;
import com.facebook.dto.groups.GroupResponse;
import com.facebook.dto.groups.GroupRoleRequest;
import com.facebook.exception.AlreadyMemberException;
import com.facebook.exception.BannedMemberException;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.GroupFacade;
import com.facebook.model.AppUser;
import com.facebook.model.groups.Group;
import com.facebook.model.groups.GroupMembers;
import com.facebook.model.groups.GroupPost;
import com.facebook.model.groups.GroupRole;
import com.facebook.model.groups.PostStatus;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.groups.GroupMembersRepository;
import com.facebook.repository.groups.GroupPostRepository;
import com.facebook.repository.groups.GroupRepository;
import com.facebook.utils.SortUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Основний сервіс для управління групами у соціальній мережі.
 * Відповідає за створення нових груп та взаємодію з репозиторіями, пов'язаними з групами.
 * Для оптимізації транзакційних викликів використовує GroupQueryService.
 */
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

    private static final String GROUP_NOT_FOUND = "Group not found with id: ";

    private static final String USER_NOT_FOUND = "User not found with id: ";

    private static final String NOT_A_MEMBER =  "User is not a member of the group";

    /**
     * Створює нову групу на основі запиту і додає користувача як адміністратора групи.
     *
     * @param groupRequest Дані для створення нової групи.
     * @param userId Ідентифікатор користувача, який створює групу.
     * @return GroupResponse - DTO з інформацією про створену групу.
     * @throws NotFoundException якщо користувач для створення групи не знайдений.
     */
    @Transactional
    public GroupResponse createGroup(GroupRequest groupRequest, Long userId) {
        Group group = modelMapper.map(groupRequest, Group.class);
        Group savedGroup = groupRepository.save(group);

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));

        GroupMembers membership = new GroupMembers();
        membership.setUser(user);
        membership.setGroup(savedGroup);
        membership.setRoles(new GroupRole[]{GroupRole.ADMIN});
        groupMembersRepository.save(membership);

        return groupQueryService.getGroupWithMembers(savedGroup.getId());
    }

    /**
     * Виконує вступ користувача до групи. Перевіряє, чи не є користувач вже членом або
     * адміністратором групи, або чи не заблокований він в цій групі.
     *
     * @param groupId Ідентифікатор групи, до якої користувач намагається вступити.
     * @param userId Ідентифікатор користувача, який намагається вступити до групи.
     * @return GroupResponse - DTO з інформацією про групу після вступу користувача.
     * @throws NotFoundException якщо група або користувач не знайдені.
     * @throws AlreadyMemberException якщо користувач вже є членом або адміністратором групи.
     * @throws BannedMemberException якщо користувач заблокований в групі.
     */
    @Transactional
    public GroupResponse joinGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND + groupId));

        Optional<GroupMembers> existingMemberOpt = groupMembersRepository
                .findByUserIdAndGroupId(userId, groupId);

        if (existingMemberOpt.isPresent()) {
            GroupMembers existingMember = existingMemberOpt.get();
            Set<GroupRole> roles = new HashSet<>(Arrays.asList(existingMember.getRoles()));

            if (roles.contains(GroupRole.ADMIN) || roles.contains(GroupRole.MEMBER)) {
                throw new AlreadyMemberException("User is already a member or admin of the group");
            } else if (roles.contains(GroupRole.BANNED)) {
                throw new BannedMemberException("User is banned from joining the group");
            }
        }

        GroupMembers newMember = new GroupMembers();
        newMember.setUser(appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId)));
        newMember.setGroup(group);
        newMember.setRoles(new GroupRole[]{GroupRole.MEMBER});
        groupMembersRepository.save(newMember);

        return groupQueryService.getGroupWithMembers(groupId);
    }

    /**
     * Отримує сторінку членів групи за їх ролями.
     * Цей метод використовує GroupRoleRequest для фільтрації членів групи в залежності від їх ролей,
     * включаючи ADMIN, MEMBER та BANNED. Також використовується пагінація та сортування.
     *
     * @param groupId Ідентифікатор групи, члени якої повинні бути знайдені.
     * @param roleRequest Запит, що містить набір ролей для фільтрації членів групи.
     * @param page Номер сторінки для пагінації.
     * @param size Кількість записів на сторінку.
     * @param sort Параметр сортування у форматі "поле,направлення".
     * @return Сторінка членів групи (GroupMembersDto), які відповідають заданим критеріям.
     * @throws NotFoundException Якщо група з заданим ідентифікатором не знайдена.
     */
    public Page<GroupMembersDto> getGroupMembersByRoles(Long groupId,
                                                        GroupRoleRequest roleRequest,
                                                        int page, int size, String sort) {

        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException(GROUP_NOT_FOUND + groupId);
        }

        return groupMembersRepository
                .findMembersByGroupIdAndRoles(groupId,
                        roleRequest.getRoles().contains(GroupRole.MEMBER) ? "MEMBER" : "",
                        roleRequest.getRoles().contains(GroupRole.ADMIN) ? "ADMIN" : "",
                        roleRequest.getRoles().contains(GroupRole.BANNED) ? "BANNED" : "",
                        PageRequest.of(page, size, SortUtils.getSorting(sort)))
                .map(groupFacade::mapToGroupMembersDto);
    }

    /**
     * Створює новий пост у групі. Метод виконує перевірку, чи користувач є членом групи,
     * а також визначає роль користувача в групі. Якщо користувач є адміністратором,
     * пост автоматично публікується. Інакше, пост зберігається як чернетка.
     *
     * @param request Об'єкт запиту для створення поста, що містить необхідну інформацію.
     * @param userId Ідентифікатор користувача, який створює пост.
     * @param groupId Ідентифікатор групи, в якій створюється пост.
     * @return Об'єкт {@link GroupPostBase}, що представляє базову інформацію про створений пост.
     * @throws NotFoundException якщо група або користувач не знайдені.
     * @throws AccessDeniedException якщо користувач не є членом групи.
     * @throws IllegalStateException якщо виникає проблема зі збереженням поста.
     */
    @Transactional
    public GroupPostBase createGroupPost(GroupPostRequest request, Long userId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND + groupId));

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));

        GroupMembers membership = groupMembersRepository
                .findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new AccessDeniedException(NOT_A_MEMBER));

        GroupPost groupPost = groupFacade
                .convertGroupPostRequestToGroupPost(request, user, group);

        if (Arrays.asList(membership.getRoles()).contains(GroupRole.ADMIN)) {
            groupPost.setStatus(PostStatus.PUBLISHED);
        } else {
            groupPost.setStatus(PostStatus.DRAFT);
        }

        return Optional.of(groupPostRepository.save(groupPost))
                .filter(savedPost -> savedPost.getId() != null)
                .flatMap(savedPost -> groupPostRepository
                        .findGroupPostDetailsById(savedPost.getId(), groupId, userId))
                .orElseThrow(() -> new IllegalStateException("Failed to save or retrieve group post"));
    }

    /**
     * Отримує деталі поста у групі. Витягує базову інформацію про пост
     * за його ідентифікатором. Якщо це репост, також отримує інформацію
     * про оригінальний пост.
     *
     * @param groupId Ідентифікатор групи, в якій розміщено пост.
     * @param postId Ідентифікатор поста, деталі якого потрібно отримати.
     * @param userId Ідентифікатор користувача, який запитує інформацію.
     * @return Об'єкт {@link GroupPostResponse}, що містить детальну інформацію про пост,
     *         включно з інформацією про оригінальний пост, якщо він існує.
     * @throws AccessDeniedException якщо користувач не є членом групи.
     * @throws NotFoundException якщо пост або оригінальний пост не знайдені.
     */
    @Transactional
    public GroupPostResponse getGroupPostDetails(Long groupId, Long postId, Long userId) {
 GroupPostBase basePost = groupPostRepository
                .findGroupPostDetailsById(postId, groupId, userId)
                .orElseThrow(() -> new NotFoundException("Group post not found with id: " + postId));

        GroupPostResponse response = new GroupPostResponse();
        BeanUtils.copyProperties(basePost, response);

        if (basePost.getOriginalPostId() != null) {
            response.setOriginalPost(groupPostRepository
                    .findGroupPostDetailsById(basePost.getOriginalPostId(), groupId, userId)
                    .map(base -> {
                        GroupPostResponse originalResponse = new GroupPostResponse();
                        BeanUtils.copyProperties(base, originalResponse);
                        return originalResponse;
                    })
                    .orElseThrow(() -> new NotFoundException("Original group post not found with id: " + basePost.getOriginalPostId())));
        }

        return response;
    }

}


