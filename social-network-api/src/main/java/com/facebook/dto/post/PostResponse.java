package com.facebook.dto.post;

import com.facebook.dto.appuser.AppUserForPost;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostResponse {

    private Long id;

    private LocalDateTime created_date;

    private LocalDateTime last_modified_date;

    private String title;

    private String body;

    private String status;

    private AppUserForPost user;

    private List<Long> comments;

    private List<Long> likes;

    private List<Long> reposts;

}
