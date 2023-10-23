package com.facebook.dto.post;

import com.facebook.model.posts.PostType;
import java.sql.Timestamp;

import lombok.Data;

/**
 * Дані про допис, отримані в результаті SQL-запиту.
 *
 * <p>Для дописів типу {@link PostType#REPOST}, додатково надаються дані про оригінальний
 * допис та його автора.</p>
 *
 * <p>Клас також містить рядкові поля для ідентифікаторів коментарів, лайків та репостів,
 * пов'язаних із дописом.</p>
 * Якщо реалізувати репости репостів додати:
 * <p>{@code private String currentReposts}</p>
 */
@Data
public class PostSqlResult {

    private Long postId;
    private Timestamp created_date;
    private Timestamp last_modified_date;
    private String imageUrl;
    private String title;
    private String body;
    private String status;
    private Long userId;
    private String name;
    private String surname;
    private String username;
    private String avatar;
    private String commentIds;
    private String likeIds;
    private PostType type;

    private Long originalUserId;
    private String originalName;
    private String originalSurname;
    private String originalUsername;
    private String originalAvatar;

    private Long originalPostId;
    private String originalTitle;
    private String originalBody;
    private String originalStatus;
    private String originalCommentIds;
    private String originalLikeIds;
    private String originalReposts;
    private PostType originalType;

    @Override
    public String toString() {
        return "PostSqlResult{"
                + "postId=" + postId
                + ", created_date=" + created_date
                + ", last_modified_date=" + last_modified_date
                + ", imageUrl='" + imageUrl + '\''
                + ", title='" + title + '\''
                + ", body='" + body + '\''
                + ", status='" + status + '\''
                + ", userId=" + userId
                + ", name='" + name + '\''
                + ", surname='" + surname + '\''
                + ", username='" + username + '\''
                + ", avatar='" + avatar + '\''
                + ", commentIds='" + commentIds + '\''
                + ", likeIds='" + likeIds + '\''
                + ", type=" + type + '\''
                + ", originalUserId=" + originalUserId
                + ", originalName='" + originalName + '\''
                + ", originalSurname='" + originalSurname + '\''
                + ", originalUsername='" + originalUsername + '\''
                + ", originalAvatar='" + originalAvatar + '\''
                + ", originalPostId=" + originalPostId
                + ", originalTitle='" + originalTitle + '\''
                + ", originalBody='" + originalBody + '\''
                + ", originalStatus='" + originalStatus + '\''
                + ", originalCommentIds='" + originalCommentIds + '\''
                + ", originalLikeIds='" + originalLikeIds + '\''
                + ", originalRepostIds='" + originalReposts + '\''
                + ", originalType=" + originalType
                + '}';
    }

}
