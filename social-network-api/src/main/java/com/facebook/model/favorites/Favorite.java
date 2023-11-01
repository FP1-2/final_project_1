package com.facebook.model.favorites;

import com.facebook.model.AbstractEntity;
import com.facebook.model.AppUser;
import com.facebook.model.posts.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Клас, який представляє сутність "Oбране".
 * Відображає відношення між користувачем та його улюбленими постами.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "favorites")
@EqualsAndHashCode(callSuper = true)
public class Favorite extends AbstractEntity {

    /**
     * Користувач, який додав пост до обраного.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private AppUser user;

    /**
     * Пост, який було додано.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

}
