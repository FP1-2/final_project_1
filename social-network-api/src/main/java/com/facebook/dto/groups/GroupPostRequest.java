package com.facebook.dto.groups;

import com.facebook.dto.post.PostRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupPostRequest extends PostRequest {

    @Override
    public String toString() {
        return "GroupPostRequest{" + super.toString() + '}';
    }

}

