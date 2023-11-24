package com.facebook.controller;

import com.facebook.dto.groups.GroupMembersDto;
import com.facebook.dto.groups.GroupRequest;
import com.facebook.dto.groups.GroupResponse;
import com.facebook.dto.groups.GroupRoleRequest;
import com.facebook.exception.AlreadyMemberException;
import com.facebook.exception.BannedMemberException;
import com.facebook.exception.NotFoundException;
import com.facebook.model.groups.GroupRole;
import com.facebook.service.CurrentUserService;
import com.facebook.service.groups.GroupQueryService;
import com.facebook.service.groups.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
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

    /**
     * Створює нову групу на основі отриманого запиту.
     *
     * @param groupRequest DTO з даними для створення групи.
     * @return ResponseEntity, що містить GroupResponse з інформацією про створену групу.
     */
    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@Validated
                                                     @RequestBody GroupRequest groupRequest) {
        Long userId = currentUserService.getCurrentUserId();
        GroupResponse newGroupResponse = groupService.createGroup(groupRequest, userId);
        return new ResponseEntity<>(newGroupResponse, HttpStatus.CREATED);
    }

    /**
     * Отримує інформацію про групу за її ідентифікатором.
     *
     * @param groupId Ідентифікатор групи, інформацію про яку потрібно отримати.
     * @return ResponseEntity, що містить GroupResponse з детальною інформацією про групу.
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable Long groupId) {
        GroupResponse groupResponse = groupQueryService.getGroupWithMembers(groupId);
        return ResponseEntity.ok(groupResponse);
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
    public ResponseEntity<GroupResponse> joinGroup(@PathVariable Long groupId) {
        Long userId = currentUserService.getCurrentUserId();
        GroupResponse response = groupService.joinGroup(groupId, userId);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<Page<GroupMembersDto>> getGroupMembersByRole(
            @PathVariable Long groupId,
            @RequestParam(required = false) List<GroupRole> roles,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        GroupRoleRequest roleRequest = new GroupRoleRequest();
        roleRequest.setRoles(new HashSet<>(roles != null ? roles : Collections.emptyList()));
        return ResponseEntity.ok(groupService.getGroupMembersByRoles(groupId, roleRequest, page, size, sort));
    }

}
