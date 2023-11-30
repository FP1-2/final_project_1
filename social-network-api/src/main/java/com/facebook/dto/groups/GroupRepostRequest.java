package com.facebook.dto.groups;

import com.facebook.dto.post.RepostRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Клас запиту для створення репосту.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GroupRepostRequest extends RepostRequest {

    @Override
    public String toString() {
        return "GroupRepostRequest{" + super.toString() + '}';
    }

}
