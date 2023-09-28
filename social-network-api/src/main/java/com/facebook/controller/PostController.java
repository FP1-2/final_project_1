package com.facebook.controller;

import com.facebook.dto.post.PostResponse;
import com.facebook.facade.PostFacade;
import com.facebook.model.posts.Post;
import com.facebook.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
@Validated
public class PostController {
    private final PostService postService;
    private final PostFacade postFacade;

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<Page<PostResponse>> getPostsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        String[] sortParts = sort.split(",");
        String property = sortParts[0];
        String direction = sortParts.length > 1 ? sortParts[1] : "asc";
        Sort sorting = direction.equalsIgnoreCase("desc") ?
                Sort.by(Sort.Order.desc(property)) : Sort.by(Sort.Order.asc(property));
        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Post> posts = postService.findByUserId(userId, pageable);
        Page<PostResponse> postResponses = posts.map(postFacade::convertToPostResponse);

        return ResponseEntity.ok(postResponses);
    }

}
