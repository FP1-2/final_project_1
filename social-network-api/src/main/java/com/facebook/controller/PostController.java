package com.facebook.controller;

import com.facebook.dto.post.PostResponse;
import com.facebook.facade.PostFacade;
import com.facebook.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
@Validated
public class PostController {
    private final PostService postService;
    private final PostFacade postFacade;

    @GetMapping("/by_user_id/{userId}")
    public ResponseEntity<Page<PostResponse>> getPostsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Page<PostResponse> postResponses = postService.findPostDetailsByUserId(userId, page, size, sort);
        return ResponseEntity.ok(postResponses);
    }

}
