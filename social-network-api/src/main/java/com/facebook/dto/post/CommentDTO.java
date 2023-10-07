package com.facebook.dto.post;

import com.facebook.dto.appuser.AppUserForPost;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private AppUserForPost appUser;

}
