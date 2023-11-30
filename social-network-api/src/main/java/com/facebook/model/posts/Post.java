package com.facebook.model.posts;

import com.facebook.model.AbstractEntity;
import com.facebook.model.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Клас, що представляє собою пост в системі. Основні поля класу:
 * <ul>
 *     <li>{@link #imageUrl} - URL зображення поста.</li>
 *     <li>{@link #title} - Заголовок поста.</li>
 *     <li>{@link #body} - Текстовий вміст поста.</li>
 *     <li>{@link #user} - Користувач, який створив пост.</li>
 *     <li>{@link #type} - Тип поста.</li>
 *     <li>{@link #originalPostId} - ID оригінального поста (у випадку репоста).</li>
 * </ul>
 * Метод {@link #isValidCombination()} перевіряє правильність комбінації типу поста
 * та ID оригінального поста. Наприклад, звичайний пост не повинен мати ID
 * оригінального поста, а репост повинен мати такий ID.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "posts")
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "post_type", discriminatorType = DiscriminatorType.STRING)
public class Post extends AbstractEntity {

    @Column(name = "image_url")
    private String imageUrl;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

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
        return String.format("Post{id=%d, ImageUrl=%s, Title=%s, Body=%s, Type=%s, OriginalPostId=%s, %s}",
                getId(), imageUrl, title, text, type, originalPostId, userFieldsForPost);
    }

}



