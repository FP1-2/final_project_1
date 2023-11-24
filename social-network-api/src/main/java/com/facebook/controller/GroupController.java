package com.facebook.controller;

import com.facebook.dto.groups.GroupRequest;
import com.facebook.dto.groups.GroupResponse;
import com.facebook.service.CurrentUserService;
import com.facebook.service.groups.GroupQueryService;
import com.facebook.service.groups.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
