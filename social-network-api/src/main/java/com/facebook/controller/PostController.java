package com.facebook.controller;

import com.facebook.dto.post.LikeResponse;
import com.facebook.dto.post.RepostResponse;
import com.facebook.dto.post.CommentResponse;
import com.facebook.dto.post.CommentRequest;
import com.facebook.dto.post.PostResponse;
import com.facebook.exception.UnauthorizedAccessException;
import com.facebook.service.CurrentUserService;
import com.facebook.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostService postService;

    private final CurrentUserService currentUserService;

    private void checkUserId(Long idAppUser) {
        Long currentUserId = currentUserService.getCurrentUserId();
        if (!idAppUser.equals(currentUserId)) {
            throw new UnauthorizedAccessException("Unauthorized access!");
        }
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<LikeResponse> likePost(@PathVariable Long postId) {
        Long userId = currentUserService.getCurrentUserId();
        return postService.likePost(userId, postId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/repost/{postId}")
    public ResponseEntity<RepostResponse> repost(@PathVariable Long postId) {
        Long userId = currentUserService.getCurrentUserId();
        return postService.repost(userId, postId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> addComment(@Validated
                                                      @RequestBody CommentRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        return postService.addComment(userId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/by_user_id/{userId}")
    public ResponseEntity<Page<PostResponse>> getPostsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Page<PostResponse> postResponses = postService
                .findPostDetailsByUserId(userId, page, size, sort);
        return ResponseEntity.ok(postResponses);
    }

}
