package com.facebook.model.posts;

import com.facebook.model.AbstractCreatedDate;
import com.facebook.model.AppUser;
import jakarta.persistence.Column;
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
@Table(name = "comments")
@EqualsAndHashCode(callSuper = true)
public class Comment extends AbstractCreatedDate {

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Override
    public String toString() {
        String commentingUser = user == null
                ? "null" : String.format("User{id=%d}", user.getId());
        String commentedPost = post == null
                ? "null" : String.format("Post{id=%d}", post.getId());
        String displayedContent = content == null ? null :
                (content.length() > 8 ? content.substring(0, 8) + "..." : content);
        return String.format("Comment{id=%d, User=%s, Post=%s, Content='%s'}",
                getId(), commentingUser, commentedPost, displayedContent);
    }

}
