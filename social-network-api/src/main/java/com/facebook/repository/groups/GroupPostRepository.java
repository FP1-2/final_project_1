package com.facebook.repository.groups;

import com.facebook.dto.groups.GroupPostBase;
import com.facebook.dto.groups.GroupPostResponse;
import com.facebook.model.groups.GroupPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Репозиторій для роботи з постами групи в соціальній мережі.
 * Дозволяє виконувати запити для отримання деталізованої інформації про пости групи,
 * включаючи дані про автора, кількість коментарів, лайків та репостів.
 */
public interface GroupPostRepository extends JpaRepository<GroupPost, Long> {

    /**
     * Шаблон запиту для отримання деталізованої інформації про пост групи.
     */
    String GROUP_POST_DETAILS_SELECT = """
            SELECT new com.facebook.dto.groups.GroupPostBase(
                gp.id,
                gp.group.id,
                gp.status,
                new com.facebook.dto.post.Author(
                    u.id AS userId,
                    u.name,
                    u.surname,
                    u.username,
                    u.avatar
                ),
                gp.createdDate,
                gp.lastModifiedDate,
                gp.imageUrl,
                gp.title,
                gp.body,
                gp.type,
                (SELECT COUNT(f.id) > 0 FROM Favorite f WHERE f.post.id = gp.id AND f.user.id = :userId),
                (SELECT COUNT(c.id) FROM Comment c WHERE c.post.id = gp.id),
                (SELECT COUNT(l.id) FROM Like l WHERE l.post.id = gp.id),
                (SELECT COUNT(r.id) FROM Post r WHERE r.originalPostId = gp.id),
                gp.originalPostId
            )
            """;

    /**
     * Запит для отримання детальної інформації про конкретний пост групи.
     *
     * @param postId Ідентифікатор поста.
     * @param groupId Ідентифікатор групи.
     * @param userId Ідентифікатор користувача (для визначення вподобань).
     * @return Опціональний об'єкт GroupPostResponse з деталями поста.
     */
    @Query(GROUP_POST_DETAILS_SELECT + """
            FROM GroupPost gp
            JOIN gp.user u
            WHERE gp.id = :postId AND gp.group.id = :groupId
            """)
    Optional<GroupPostBase> findGroupPostDetailsById(@Param("postId") Long postId,
                                                     @Param("groupId") Long groupId,
                                                     @Param("userId") Long userId);

    /**
     * Запит для отримання сторінки детальної інформації про всі пости певної групи.
     *
     * @param groupId Ідентифікатор групи.
     * @param userId Ідентифікатор користувача.
     * @param pageable Параметри пагінації та сортування.
     * @return Сторінка об'єктів GroupPostResponse з деталями постів.
     */
    @Query(GROUP_POST_DETAILS_SELECT + """
       FROM GroupPost gp
       JOIN gp.user u
       WHERE gp.group.id = :groupId
         AND (:userId IS NULL OR gp.user.id = :userId)
       """)
    Page<GroupPostResponse> findAllGroupPostDetailsByGroupId(@Param("groupId") Long groupId,
                                                             @Param("userId") Long userId,
                                                             Pageable pageable);

}

