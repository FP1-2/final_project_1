package com.facebook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id",
            nullable = false)
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



