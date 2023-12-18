package com.facebook.controller;

import com.facebook.dto.groups.GroupMember;
import com.facebook.dto.groups.GroupPostRequest;
import com.facebook.dto.groups.GroupPostResponse;
import com.facebook.dto.groups.GroupRepostRequest;
import com.facebook.dto.groups.GroupRequest;
import com.facebook.dto.groups.GroupResponse;
import com.facebook.dto.groups.GroupRoleRequest;
import com.facebook.dto.groups.PostStatusRequest;
import com.facebook.dto.post.ActionResponse;
import com.facebook.dto.post.CommentRequest;
import com.facebook.dto.post.CommentResponse;
import com.facebook.exception.AlreadyMemberException;
import com.facebook.exception.BannedMemberException;
import com.facebook.exception.GroupNotFoundException;
import com.facebook.exception.NotFoundException;
import com.facebook.model.groups.GroupRole;
import com.facebook.model.groups.PostStatus;
import com.facebook.service.CurrentUserService;
import com.facebook.service.PostService;
import com.facebook.service.groups.GroupQueryService;
import com.facebook.service.groups.GroupService;
import com.facebook.utils.SortUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Контролер для управління групами в соціальній мережі.
 * Обробляє HTTP-запити, пов'язані з діями над групами.
 */
@Log4j2
@Validated
@RestController
@RequestMapping("api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    private final CurrentUserService currentUserService;

    private final GroupQueryService groupQueryService;

    private final PostService postService;

    /**
     * Створює нову групу на основі отриманого запиту.
     *
     * @param groupRequest DTO з даними для створення групи.
     * @return ResponseEntity, що містить GroupResponse з інформацією про створену групу.
     */
    @PostMapping
    public ResponseEntity<GroupResponse>
    createGroup(@Validated
                @RequestBody GroupRequest groupRequest) {
        return new ResponseEntity<>(groupService
                .createGroup(groupRequest,
                        currentUserService.getCurrentUserId()),
                HttpStatus.CREATED);
    }

    /**
     * Отримує інформацію про групу за її ідентифікатором.
     *
     * @param groupId Ідентифікатор групи, інформацію про яку потрібно отримати.
     * @return ResponseEntity, що містить GroupResponse з детальною інформацією про групу.
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse>
    getGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupQueryService
                .getGroupWithMembers(groupId));
    }

    /**
     * Обробляє запит на приєднання користувача до групи.
     * Даний метод дозволяє поточному авторизованому користувачу приєднатися до групи за її ідентифікатором.
     * Після успішного приєднання користувач стає членом групи з роллю MEMBER.
     *
     * @param groupId Ідентифікатор групи, до якої користувач бажає приєднатися.
     * @return Відповідь ResponseEntity, що містить інформацію про групу (GroupResponse) після приєднання.
     * @throws NotFoundException Якщо група з заданим ідентифікатором не знайдена.
     * @throws AlreadyMemberException Якщо користувач вже є членом або адміністратором групи.
     * @throws BannedMemberException Якщо користувач заблокований у групі.
     */
    @PostMapping("/{groupId}/join")
    public ResponseEntity<GroupResponse>
    joinGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService
                .joinGroup(groupId,
                        currentUserService.getCurrentUserId()));
    }

    /**
     * Отримує сторінку членів групи за заданим ідентифікатором групи та ролями.
     * Цей метод обробляє GET-запити для отримання членів групи з урахуванням їх ролей
     * (ADMIN, MEMBER, BANNED) та підтримує пагінацію та сортування.
     * Ролі передаються через параметри запиту в URL.
     *
     * <p>Приклад запиту в Postman:</p>
     * <code>
     * GET http://localhost:9000/api/groups/1/members?page=0&size=10&sort=id,desc&roles=ADMIN&roles=MEMBER
     * </code>
     *
     * @param groupId Ідентифікатор групи, члени якої повинні бути отримані.
     * @param roles Список ролей для фільтрації членів групи.
     * @param page Номер сторінки для пагінації.
     * @param size Кількість записів на сторінку.
     * @param sort Параметр сортування у форматі "поле,направлення".
     * @return Відповідь ResponseEntity, що містить сторінку членів групи (GroupMembersDto).
     */
    @GetMapping("/{groupId}/members")
    public ResponseEntity<Page<GroupMember>>
    getGroupMembersByRole(
            @PathVariable Long groupId,
            @RequestParam(required = false) List<GroupRole> roles,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        GroupRoleRequest roleRequest = new GroupRoleRequest();

        roleRequest.setRoles(new HashSet<>(roles != null
                ? roles : Collections.emptyList()));

        Pageable pageable = PageRequest.of(page,
                size, SortUtils.getSorting(sort));

        return ResponseEntity.ok(groupService
                .getGroupMembersByRoles(groupId,
                        roleRequest, pageable));
    }

    /**
     * Створює новий пост у групі.
     *
     * @param groupId Ідентифікатор групи, в якій створюється пост.
     * @param request Тіло запиту, що містить дані для створення посту.
     * @return Відповідь із створеним постом та статусом HTTP CREATED.
     */
    @PostMapping("/{groupId}/posts")
    public ResponseEntity<GroupPostResponse>
    createGroupPost(@PathVariable Long groupId,
                    @Validated @RequestBody GroupPostRequest request) {
        return new ResponseEntity<>(groupService
                .createGroupPost(request,
                        currentUserService.getCurrentUserId(),
                        groupId), HttpStatus.CREATED);
    }

    /**
     * Створює репост для поста в групі.
     *
     * @param groupId Ідентифікатор групи, в якій створюється репост.
     * @param request Об'єкт запиту для створення репосту.
     * @return ResponseEntity з об'єктом GroupPostResponse,
     * що відображає деталі створеного репосту, та статусом CREATED.
     */
    @PostMapping("/{groupId}/reposts")
    public ResponseEntity<GroupPostResponse>
    createGroupRepost(@PathVariable Long groupId,
                    @Validated @RequestBody GroupRepostRequest request) {
        return new ResponseEntity<>(groupService
                .createGroupRepost(request,
                        currentUserService.getCurrentUserId(),
                        groupId), HttpStatus.CREATED);
    }

    /**
     * Ендпойнт для отримання деталей поста у групі.
     * Перевіряє, чи є поточний користувач членом групи та забезпечує доступ до детальної інформації про пост.
     * Якщо пост є репостом, також повертає інформацію про оригінальний пост.
     *
     * @param groupId Ідентифікатор групи, в якій знаходиться пост.
     * @param postId Ідентифікатор поста, деталі якого потрібно отримати.
     * @return ResponseEntity, що містить {@link GroupPostResponse} з деталями поста.
     */
    @GetMapping("/{groupId}/posts/{postId}")
    public ResponseEntity<GroupPostResponse>
    getGroupPostDetails(
            @PathVariable Long groupId,
            @PathVariable Long postId) {
        return ResponseEntity.ok(groupService
                .getGroupPostDetails(groupId,
                        postId,
                        currentUserService.getCurrentUserId()));
    }

    /**
     * Отримує сторінку постів групи за заданими параметрами.
     *
     * @param groupId Ідентифікатор групи.
     * @param user Ідентифікатор користувача для фільтрації постів.
     * @param statuses Список статусів постів для фільтрації.
     * @param page Номер сторінки для пагінації.
     * @param size Розмір сторінки для пагінації.
     * @param sort Параметри сортування.
     * @return ResponseEntity зі сторінкою відповідей на пости групи.
     *
     * <p> Приклад URL-адреси для використання в Postman:
     * {@code
     *   http://localhost:9000/api/groups/1/posts?user=3&statuses=DRAFT,PUBLISHED&page=0&size=10&sort=id,desc
     * }
     * У цьому прикладі запитується сторінка постів для групи з ідентифікатором 1,
     * фільтрація постів виконується за користувачем з ідентифікатором 3 і статусами DRAFT та PUBLISHED.
     * Пагінація встановлена на першу сторінку (page=0) з 10 записами на сторінку,
     * і сортування виконується за ідентифікатором постів у спадному порядку.
     * </p>
     */
    @GetMapping("/{groupId}/posts")
    public ResponseEntity<Page<GroupPostResponse>> getAllGroupPosts(
            @PathVariable Long groupId,
            @RequestParam(required = false) Long user,
            @RequestParam(required = false) List<PostStatus> statuses,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Pageable pageable = PageRequest.of(page, size, SortUtils.getSorting(sort));
        Page<GroupPostResponse> posts = groupService
                .getAllGroupPosts(groupId,
        //id поточного авторизованого користувача для встановлення поля is Favorite
                        currentUserService.getCurrentUserId(),
        //id користувача для фільтрації постів групи
                        user,
                        PostStatusRequest.of(statuses),
                        pageable);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<ActionResponse> likeGroupPost(@PathVariable Long postId) {
        postService.checkPostType(postId, "GroupPost");
        Long userId = currentUserService.getCurrentUserId();
        return postService.likePost(userId, postId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> addComment(@Validated
                                                      @RequestBody
                                                      CommentRequest commentRequest) {
        Long userId = currentUserService.getCurrentUserId();
        groupService.checkMembership(userId, Optional.ofNullable(commentRequest.getGroupId())

                .orElseThrow(() -> new GroupNotFoundException("Group ID is required")));
        postService.checkPostType(commentRequest.getPostId(), "GroupPost");

        return postService.addComment(userId, commentRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * Отримує всі групи, в яких користувач є учасником або адміністратором.
     * Якщо роль не передається, то повертаються всі групи, в яких користувач є учасником.
     *
     * @param userId      ID користувача, для якого потрібно отримати групи. Цей параметр передається в шляху запиту.
     * @param role        Роль користувача у групі. Це необов'язковий параметр. Якщо він не вказаний,
     *                    будуть повернуті всі групи, в яких користувач є учасником.
     * @param page        Номер сторінки пагінації. За замовчуванням - 0.
     * @param size        Розмір сторінки пагінації. За замовчуванням - 10.
     * @param sort        Параметри сортування. За замовчуванням - за датою створення в порядку спадання.
     * @return            Відповідь зі сторінкою об'єктів {@link GroupResponse}, яка містить дані про групи.
     *
     * Приклад запиту:
     * GET https://yourhostel.world/api/groups/user/123?page=0&size=10&sort=createdDate,desc
     * Тут 123 - це userId. Без параметра role повертаються всі групи користувача.
     *
     * GET https://yourhostel.world/api/groups/user/123?page=0&size=10&sort=createdDate,desc&role=ADMIN
     * Тут role=ADMIN означає, що будуть повернуті групи, де користувач є адміністратором.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<GroupResponse>> getAllGroupsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate,desc") String sort) {
        Page<GroupResponse> groups = groupQueryService.getAllGroupsByUser(userId, role, page, size, sort);
        return ResponseEntity.ok(groups);
    }

}
