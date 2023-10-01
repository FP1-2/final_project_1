package com.facebook.facade;

import com.facebook.dto.appuser.AppUserForPost;
import com.facebook.dto.post.PostResponse;
import com.facebook.dto.post.PostSqlResult;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostFacade {

    private final ModelMapper modelMapper;

    public PostSqlResult mapToPostSqlResult(Map<String, Object> row) {
        return modelMapper.map(row, PostSqlResult.class);
    }

    public PostResponse convertToPostResponse(Map<String, Object> row) {
        PostSqlResult sqlResult = mapToPostSqlResult(row);
        PostResponse response = modelMapper.map(sqlResult, PostResponse.class);
        AppUserForPost userForPost = modelMapper.map(sqlResult, AppUserForPost.class);
        response.setUser(userForPost);

        return response;
    }
}