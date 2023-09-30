package com.facebook.dto.post;

import com.facebook.dto.appuser.AppUserForPost;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private Long id;

    private LocalDateTime created_date;

    private LocalDateTime last_modified_date;

    private String title;

    private String body;

    private String status;

    private AppUserForPost user;

}
