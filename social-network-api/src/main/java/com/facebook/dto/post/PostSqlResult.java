package com.facebook.dto.post;

import java.sql.Clob;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class PostSqlResult {

    private Long id;

    private Timestamp created_date;

    private Timestamp last_modified_date;

    private String title;

    private Clob body;

    private String status;

    private Long user_id;

    private String name;

    private String surname;

    private String username;

    private String avatar;

    private String comment_ids;

    private String like_ids;

    private String repost_ids;
}
