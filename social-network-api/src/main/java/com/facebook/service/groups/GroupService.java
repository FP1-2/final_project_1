package com.facebook.service.groups;

import com.facebook.dto.groups.GroupMember;
import com.facebook.dto.groups.GroupPostBase;
import com.facebook.dto.groups.GroupPostRequest;
import com.facebook.dto.groups.GroupPostResponse;
import com.facebook.dto.groups.GroupRepostRequest;
import com.facebook.dto.groups.GroupRequest;
import com.facebook.dto.groups.GroupResponse;
import com.facebook.dto.groups.GroupRoleRequest;
import com.facebook.dto.groups.PostStatusRequest;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private static final String USER_IS_BANNED = "User is banned from joining the group";
    private static final String ALREADY_A_MEMBER = "User is already a member or admin of the group.";
    private static final String GROUP_NOT_FOUND = "Group not found with id: ";
    private static final String USER_NOT_FOUND = "User not found with id: ";
    private static final String NOT_A_MEMBER = "User is not a member of the group";
    private static final String FAILED_TO_SAVE = "Failed to save or retrieve group";
    private static final String DRAFT_STATUS_ACCESS_DENIED_MESSAGE = "You cannot access"
            + " this post as it is currently in draft status.";
    private static final String ARCHIVED_STATUS_ACCESS_DENIED_MESSAGE = "This post is archived"
            + " and no longer accessible.";
    private static final String REJECTED_STATUS_ACCESS_DENIED_MESSAGE = "Access is denied"
            + " as this post is marked for deletion.";
    private static final String ORIGINAL_GROUP_POST_NOT_FOUND = "Original group post not found with id: ";
    private static final String GROUP_POST_NOT_FOUND = "Group post not found with id: ";
    private static final String ALREADY_PUBLISHED = "Post is already published.";
    private static final String UNKNOWN_POST_STATUS = "Unknown post status encountered.";

    /**
     * Створює нову групу на основі запиту і додає користувача як адміністратора групи.
     *
     * @param groupRequest Дані для створення нової групи.
     * @param userId       Ідентифікатор користувача, який створює групу.
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
     * @param userId  Ідентифікатор користувача, який намагається вступити до групи.
     * @return GroupResponse - DTO з інформацією про групу після вступу користувача.
     * @throws NotFoundException      якщо група або користувач не знайдені.
     * @throws AlreadyMemberException якщо користувач вже є членом або адміністратором групи.
     * @throws BannedMemberException  якщо користувач заблокований в групі.
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
                throw new AlreadyMemberException(ALREADY_A_MEMBER);

            } else if (roles.contains(GroupRole.BANNED)) {
                throw new BannedMemberException(USER_IS_BANNED);

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
     * @param groupId     Ідентифікатор групи, члени якої повинні бути знайдені.
     * @param roleRequest Запит, що містить набір ролей для фільтрації членів групи.
     * @param pageable    Параметри пагінації.
     * @return Сторінка членів групи (GroupMembersDto), які відповідають заданим критеріям.
     * @throws NotFoundException Якщо група з заданим ідентифікатором не знайдена.
     */
    public Page<GroupMember> getGroupMembersByRoles(Long groupId,
                                                    GroupRoleRequest roleRequest,
                                                    Pageable pageable) {

        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException(GROUP_NOT_FOUND + groupId);
        }

        return groupMembersRepository
                .findMembersByGroupIdAndRoles(groupId,
                        roleRequest.getRoles().contains(GroupRole.MEMBER) ? "MEMBER" : "",
                        roleRequest.getRoles().contains(GroupRole.ADMIN) ? "ADMIN" : "",
                        roleRequest.getRoles().contains(GroupRole.BANNED) ? "BANNED" : "",
                        pageable)
                .map(groupFacade::mapToGroupMembersDto);
    }

    /**
     * Створює новий пост у групі. Метод виконує перевірку, чи користувач є членом групи,
     * а також визначає роль користувача в групі. Якщо користувач є адміністратором,
     * пост автоматично публікується. Інакше, пост зберігається як чернетка.
     *
     * @param request Об'єкт запиту для створення поста, що містить необхідну інформацію.
     * @param userId  Ідентифікатор користувача, який створює пост.
     * @param groupId Ідентифікатор групи, в якій створюється пост.
     * @return Об'єкт {@link GroupPostBase}, що представляє базову інформацію про створений пост.
     * @throws NotFoundException     якщо група або користувач не знайдені.
     * @throws AccessDeniedException якщо користувач не є членом групи.
     * @throws IllegalStateException якщо виникає проблема зі збереженням поста.
     */
    @Transactional
    public GroupPostResponse createGroupPost(GroupPostRequest request,
                                             Long userId,
                                             Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND + groupId));

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));

        GroupMembers members = groupMembersRepository
                .findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new AccessDeniedException(NOT_A_MEMBER));

        GroupPost groupPost = groupFacade
                .convertGroupPostRequestToGroupPost(request, user, group);

        if (Arrays.asList(members.getRoles()).contains(GroupRole.ADMIN)) {
            groupPost.setStatus(PostStatus.PUBLISHED);
        } else {
            groupPost.setStatus(PostStatus.DRAFT);
        }

        return Optional.of(groupPostRepository.save(groupPost))
                .filter(savedPost -> savedPost.getId() != null)
                .flatMap(savedPost -> groupPostRepository
                        .findGroupPostDetailsById(savedPost.getId(), groupId, userId))
                .map(base -> groupFacade.convertGroupPostBaseToGroupPostResponse(base, members))
                .orElseThrow(() -> new IllegalStateException(FAILED_TO_SAVE + " post"));
    }

    /**
     * Створює репост у вказаній групі від імені вказаного користувача. Метод виконує перевірку наявності групи, користувача та членства користувача у групі.
     * Також перевіряється, що оригінальний пост існує та доступний для репосту (не архівований чи відхилений). Залежно від ролі користувача у групі,
     * статус репосту може бути встановлений як опублікований або чорновик. Після збереження репосту в базу даних, виконується його отримання
     * та конвертація у GroupPostResponse, де також додається інформація про оригінальний пост.
     *
     * @param request Об'єкт запиту для створення репосту.
     * @param userId  Ідентифікатор користувача, який робить репост.
     * @param groupId Ідентифікатор групи, у якій робиться репост.
     * @return GroupPostResponse - об'єкт відповіді, що містить інформацію про створений репост.
     * @throws NotFoundException     Якщо не знайдено групу, користувача або оригінальний пост.
     * @throws AccessDeniedException Якщо користувач не є членом групи або оригінальний пост не доступний для репосту.
     * @throws IllegalStateException Якщо виникає помилка під час збереження репосту.
     */
    @Transactional
    public GroupPostResponse createGroupRepost(GroupRepostRequest request,
                                               Long userId,
                                               Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException(GROUP_NOT_FOUND + groupId));

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));

        GroupMembers members = groupMembersRepository
                .findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new AccessDeniedException(NOT_A_MEMBER));

        GroupPost groupPost = groupFacade
                .convertGroupRepostRequestToGroupPost(request, user, group);

        GroupPostBase originalPost = groupPostRepository
                .findGroupPostDetailsById(request.getOriginalPostId(), groupId, userId)
                .orElseThrow(() -> new NotFoundException(ORIGINAL_GROUP_POST_NOT_FOUND
                        + request.getOriginalPostId()));
        checkPostStatus(originalPost.getStatus());

        if (Arrays.asList(members.getRoles()).contains(GroupRole.ADMIN)) {
            groupPost.setStatus(PostStatus.PUBLISHED);
        } else groupPost.setStatus(PostStatus.DRAFT);

        return Optional.of(groupPostRepository.save(groupPost))
                .filter(savedPost -> savedPost.getId() != null)
                .flatMap(savedPost -> groupPostRepository
                        .findGroupPostDetailsById(savedPost.getId(), groupId, userId))
                .map(basePost -> {
                    GroupPostResponse response = groupFacade
                            .convertGroupPostBaseToGroupPostResponse(basePost, members);
                    addOriginalPost(response, basePost, groupId, userId);
                    return response;
                })
                .orElseThrow(() -> new IllegalStateException(FAILED_TO_SAVE + " repost"));
    }

    /**
     * Отримує деталі поста у групі. Витягує базову інформацію про пост
     * за його ідентифікатором. Якщо це репост, також отримує інформацію
     * про оригінальний пост.
     *
     * @param groupId Ідентифікатор групи, в якій розміщено пост.
     * @param postId  Ідентифікатор поста, деталі якого потрібно отримати.
     * @param userId  Ідентифікатор користувача, який запитує інформацію.
     * @return Об'єкт {@link GroupPostResponse}, що містить детальну інформацію про пост,
     * включно з інформацією про оригінальний пост, якщо він існує.
     * @throws AccessDeniedException якщо користувач не є членом групи.
     * @throws NotFoundException     якщо пост або оригінальний пост не знайдені.
     */
    @Transactional
    public GroupPostResponse getGroupPostDetails(Long groupId,
                                                 Long postId,
                                                 Long userId) {
        GroupPostBase basePost = groupPostRepository
                .findGroupPostDetailsById(postId, groupId, userId)
                .orElseThrow(() -> new NotFoundException(GROUP_POST_NOT_FOUND + postId));

        GroupMembers members = groupMembersRepository
                .findByUserIdAndGroupId(basePost.getAuthor().getUserId(), groupId)
                .orElseThrow(() -> new AccessDeniedException(NOT_A_MEMBER));

        GroupPostResponse response = groupFacade
                .convertGroupPostBaseToGroupPostResponse(basePost, members);

        if (basePost.getOriginalPostId() != null) {
            addOriginalPost(response, basePost, groupId, userId);
        }

        return response;
    }

    /**
     * Додає інформацію про оригінальний пост у відповідь, якщо пост є репостом.
     * Метод виконує перевірку наявності оригінального поста та членства автора оригінального поста у групі.
     * Якщо оригінальний пост знайдено і користувач є членом групи, інформація про оригінальний пост конвертується
     * та додається до відповіді.
     *
     * @param response Відповідь, до якої буде додано оригінальний пост.
     * @param basePost Базова інформація про поточний пост.
     * @param groupId  Ідентифікатор групи, у якій знаходиться пост.
     * @param userId   Ідентифікатор користувача, який виконує запит.
     * @throws NotFoundException     Якщо не знайдено оригінальний пост або користувача.
     * @throws AccessDeniedException Якщо автор оригінального поста не є членом групи.
     */
    private void addOriginalPost(@NotNull GroupPostResponse response,
                                 @NotNull GroupPostBase basePost,
                                 Long groupId,
                                 Long userId) {
        response.setOriginalPost(groupPostRepository
                .findGroupPostDetailsById(basePost.getOriginalPostId(),
                        groupId, userId)
                .map(base -> {
                    GroupMembers membersOriginalPost = groupMembersRepository
                            .findByUserIdAndGroupId(base.getAuthor().getUserId(), groupId)
                            .orElseThrow(() -> new AccessDeniedException(NOT_A_MEMBER));
                    return groupFacade
                            .convertGroupPostBaseToGroupPostResponse(base, membersOriginalPost);
                })
                .orElseThrow(() -> new NotFoundException(ORIGINAL_GROUP_POST_NOT_FOUND
                        + basePost.getOriginalPostId())));
    }

    /**
     * Перевіряє статус поста та кидає виняток AccessDeniedException, якщо статус не дозволяє дій.
     * Цей метод забезпечує обмеження на репост або взаємодію з постами, які мають певний статус.
     *
     * @param status Статус поста, який необхідно перевірити.
     * @throws AccessDeniedException Якщо статус поста є DRAFT, ARCHIVED або REJECTED.
     */
    private void checkPostStatus(PostStatus status) {
        switch (status) {
            case DRAFT -> throw new AccessDeniedException(DRAFT_STATUS_ACCESS_DENIED_MESSAGE);
            case ARCHIVED -> throw new AccessDeniedException(ARCHIVED_STATUS_ACCESS_DENIED_MESSAGE);
            case REJECTED -> throw new AccessDeniedException(REJECTED_STATUS_ACCESS_DENIED_MESSAGE);
            case PUBLISHED -> log.info(ALREADY_PUBLISHED);
            default -> throw new AccessDeniedException(UNKNOWN_POST_STATUS);
        }
    }

    /**
     * Отримує сторінку відповідей на пости групи з урахуванням фільтрації за статусами постів,
     * ідентифікатором користувача та іншими параметрами.
     *
     * @param groupId Ідентифікатор групи, пости якої потрібно отримати.
     * @param userId Ідентифікатор поточного авторизованого користувача для встановлення поля isFavorite.
     * @param user Ідентифікатор користувача для фільтрації постів групи.
     * @param postStatus Об'єкт PostStatusRequest, який містить статуси для фільтрації.
     * @param pageable Параметри пагінації.
     * @return Сторінка відповідей на пости групи у вигляді Page<GroupPostResponse>.
     */
    @Transactional
    public Page<GroupPostResponse> getAllGroupPosts(Long groupId,
                                                    Long userId,
                                                    Long user,
                                                    @NotNull PostStatusRequest postStatus,
                                                    Pageable pageable) {

        Page<GroupPostBase> groupPostBases = groupPostRepository
                .findAllGroupPostDetailsByGroupId(groupId,
        //id поточного авторизованого користувача для встановлення поля is Favorite
                        userId,
        //id користувача для фільтрації постів групи
                        user,
                        postStatus.getStatuses().contains(PostStatus.DRAFT) ? PostStatus.DRAFT : null,
                        postStatus.getStatuses().contains(PostStatus.PUBLISHED) ? PostStatus.PUBLISHED : null,
                        postStatus.getStatuses().contains(PostStatus.ARCHIVED) ? PostStatus.ARCHIVED : null,
                        postStatus.getStatuses().contains(PostStatus.REJECTED) ? PostStatus.REJECTED : null,
                        pageable);

        Set<Long> uniqueUserIds = new HashSet<>();
        Set<Long> uniqueOriginalIds = new HashSet<>();

        groupPostBases.forEach(post -> {
            uniqueUserIds.add(post.getAuthor().getUserId());
            if (post.getOriginalPostId() != null) {
                uniqueOriginalIds.add(post.getOriginalPostId());
            }
        });

        List<GroupPostResponse> responses = groupPostBases.getContent().stream()
                .map(postBase -> groupFacade
                        .convertToGroupPostResponse(postBase,
                        groupMembersRepository
                                .findAllByGroupIdAndUserIds(groupId, uniqueUserIds)
                                .stream()
                                .collect(Collectors
                                        .toMap(member -> member.getUser().getId(),
                                        groupFacade::mapToGroupMembersDto)),
                        groupPostRepository
                                .findAllByGroupIdAndPostIds(groupId, userId, uniqueOriginalIds)
                                .stream()
                                .collect(Collectors
                                        .toMap(GroupPostBase::getId, Function.identity()))))
                .toList();

        return new PageImpl<>(responses, pageable, groupPostBases.getTotalElements());
    }

    public void checkMembership(Long userId, Long groupId) {
        if (!groupMembersRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new AccessDeniedException(NOT_A_MEMBER);
        }
    }

}