package com.facebook.facade;

import com.facebook.dto.appuser.AppUserForPost;
import com.facebook.dto.post.PostResponse;
import com.facebook.model.posts.Post;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostFacade {

    private final ModelMapper modelMapper;

    public PostResponse convertToPostResponse(Post post) {
        PostResponse response = modelMapper.map(post, PostResponse.class);
        response.setCreated_date(post.getCreatedDate());
        response.setLast_modified_date(post.getLastModifiedDate());
        AppUserForPost userForPost = modelMapper.map(post.getUser(), AppUserForPost.class);
        response.setUser(userForPost);
        return response;
    }

}