package com.facebook.model.posts;

import com.facebook.model.AbstractEntity;
import com.facebook.model.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "posts")
@EqualsAndHashCode(callSuper = true)
public class Post extends AbstractEntity {

    @Column(name = "image_url")
    private String imageUrl;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser user;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType type;

    @Column(name = "original_post_id")
    private Long originalPostId;

    @AssertTrue(message = "Invalid combination of type and originalPostId")
    public boolean isValidCombination() {
        if (type == PostType.POST && originalPostId != null) return false;
        return type != PostType.REPOST || originalPostId != null;
    }

    @Override
    public String toString() {
        String text = body == null ? null :
                (body.length() > 8 ? body.substring(0, 8) + "..." : body);
        String userFieldsForPost = user == null ? "null" :
                String.format("User{id=%d, Name=%s, Surname=%s, Username=%s, Avatar=%s}",
                        user.getId(), user.getName(), user.getSurname(),
                        user.getUsername(), user.getAvatar());
        return String.format("Post{id=%d, ImageUrl=%s, Title=%s, Body=%s, Status=%s, Type=%s, OriginalPostId=%s, %s}",
                getId(), imageUrl, title, text, status, type, originalPostId, userFieldsForPost);
    }

}



