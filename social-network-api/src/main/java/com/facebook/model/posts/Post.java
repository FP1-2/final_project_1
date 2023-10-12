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
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    @Column(name = "image_url")
    private String imageUrl;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser user;

    @Override
    public String toString() {
        String text = body == null ? null :
                (body.length() > 8 ? body.substring(0, 8) + "..." : body);
        String userFieldsForPost = user == null ? "null" :
                String.format("User{id=%d, Name=%s, Surname=%s, Username=%s, Avatar=%s}",
                        user.getId(), user.getName(), user.getSurname(),
                        user.getUsername(), user.getAvatar());
        return String.format("Post{id=%d, ImageUrl=%s, Title=%s, Body=%s, Status=%s, %s}",
                getId(), imageUrl, title, text, status, userFieldsForPost);
    }

}



