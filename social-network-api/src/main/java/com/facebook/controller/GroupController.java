package com.facebook.controller;

import com.facebook.dto.groups.GroupRequest;
import com.facebook.dto.groups.GroupResponse;
import com.facebook.service.CurrentUserService;
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

@Log4j2
@Validated
@RestController
@RequestMapping("api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    private final CurrentUserService currentUserService;

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@Validated
                                                     @RequestBody GroupRequest groupRequest) {
        Long userId = currentUserService.getCurrentUserId();
        GroupResponse newGroupResponse = groupService.createGroup(groupRequest, userId);
        return new ResponseEntity<>(newGroupResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable Long groupId) {
        GroupResponse groupResponse = groupService.getGroupWithMembers(groupId);
        return ResponseEntity.ok(groupResponse);
    }

}
