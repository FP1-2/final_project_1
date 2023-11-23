package com.facebook.model.groups;

import com.facebook.model.posts.Post;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "group_posts")
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("group_posts")
public class GroupPost extends Post {

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private Group group;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

}
