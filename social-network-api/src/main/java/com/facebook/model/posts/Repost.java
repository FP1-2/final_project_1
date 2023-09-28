package com.facebook.model.posts;

import com.facebook.model.AbstractCreatedDate;
import com.facebook.model.AppUser;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "reposts")
@EqualsAndHashCode(callSuper = true)
public class Repost extends AbstractCreatedDate {

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @Override
    public String toString() {
        String repostingUser = user == null
                ? "null" : String.format("User{id=%d}", user.getId());
        String repostedPost = post == null
                ? "null" : String.format("Post{id=%d}", post.getId());
        return String.format("Repost{id=%d, User=%s, Post=%s, CreatedDate=%s}",
                getId(), repostingUser, repostedPost, getCreatedDate());
    }
}
