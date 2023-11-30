package com.facebook.dto.groups;

import com.facebook.model.groups.PostStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class PostStatusRequest {

    @NotNull(message = "Status cannot be null")
    private Set<PostStatus> statuses;

    private PostStatusRequest(List<PostStatus> statuses){
        this.statuses = new HashSet<>(statuses != null
                ? statuses : Collections.emptyList());
    }

    public static PostStatusRequest of(List<PostStatus> statuses){
        return new PostStatusRequest(statuses);
    }

}