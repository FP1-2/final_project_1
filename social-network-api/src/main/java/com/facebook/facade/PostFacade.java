package com.facebook.facade;

import com.facebook.dto.appuser.AppUserForPost;
import com.facebook.dto.post.PostResponse;
import com.facebook.dto.post.PostSqlResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
        PostResponse response = new PostResponse();

        modelMapper.map(sqlResult, response);

        response.setComments(stringToList(sqlResult.getComment_ids()));
        response.setLikes(stringToList(sqlResult.getLike_ids()));
        response.setReposts(stringToList(sqlResult.getRepost_ids()));

        response.setUser(modelMapper.map(sqlResult, AppUserForPost.class));

        return response;
    }

    private List<Long> stringToList(String source) {
        return Optional.ofNullable(source)
                .filter(input -> !input.isEmpty())
                .map(input -> Arrays.stream(input.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }
}