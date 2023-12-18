package com.facebook.repository.groups;

import com.facebook.dto.groups.GroupPostBase;
import com.facebook.model.groups.GroupPost;
import com.facebook.model.groups.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                gp.imageUrl AS postImageUrl,
                g.imageUrl AS groupImageUrl,
                g.name AS groupName,
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
            JOIN gp.group g
            WHERE gp.id = :postId AND gp.group.id = :groupId
            """)
    Optional<GroupPostBase> findGroupPostDetailsById(@Param("postId") Long postId,
                                                     @Param("groupId") Long groupId,
                                                     @Param("userId") Long userId);

    /**
     * Знаходить всі деталі постів групи з урахуванням ідентифікатора групи,
     * користувача та статусів постів.
     *
     * @param groupId Ідентифікатор групи.
     * @param userId Ідентифікатор користувача для встановлення поля isFavorite.
     * @param user Ідентифікатор користувача для фільтрації постів.
     * @param draft Статус поста DRAFT для фільтрації.
     * @param published Статус поста PUBLISHED для фільтрації.
     * @param archived Статус поста ARCHIVED для фільтрації.
     * @param rejected Статус поста REJECTED для фільтрації.
     * @param pageable Параметри пагінації.
     * @return Сторінка постів групи у вигляді Page<GroupPostBase>.
     */
    @Query(GROUP_POST_DETAILS_SELECT + """
            FROM GroupPost gp
            JOIN gp.user u
            JOIN gp.group g
            WHERE gp.group.id = :groupId
              AND (:user IS NULL OR gp.user.id = :user)
              AND (1=1 OR :userId IS NULL)
              AND ( (:draft = null AND :published = null AND :archived = null AND :rejected = null)
                                OR (:draft != null AND gp.status = :draft)
                                OR (:published != null AND gp.status = :published)
                                OR (:archived != null AND gp.status = :archived)
                                OR (:rejected != null AND gp.status = :rejected))
            """)
    Page<GroupPostBase> findAllGroupPostDetailsByGroupId(@Param("groupId") Long groupId,
             //id поточного авторизованого користувача для встановлення поля is Favorite
                                                         @Param("userId") Long userId,
                                            //id користувача для фільтрації постів групи
                                                         @Param("user") Long user,
                                                         @Param("draft") PostStatus draft,
                                                         @Param("published") PostStatus published,
                                                         @Param("archived") PostStatus archived,
                                                         @Param("rejected") PostStatus rejected,
                                                         Pageable pageable);

    /**
     * Знаходить всі пости групи за ідентифікаторами.
     *
     * @param groupId Ідентифікатор групи.
     * @param userId Ідентифікатор користувача.
     * @param postIds Набір ідентифікаторів постів.
     * @return Список GroupPostBase.
     */
        @Query(GROUP_POST_DETAILS_SELECT + """
           FROM GroupPost gp
           JOIN gp.user u
           JOIN gp.group g
           WHERE gp.group.id = :groupId
             AND gp.id IN :postIds
           """)
        List<GroupPostBase> findAllByGroupIdAndPostIds(@Param("groupId") Long groupId,
                                                       @Param("userId") Long userId,
                                                       @Param("postIds") Set<Long> postIds);

    @Modifying
    @Query("UPDATE GroupPost gp SET gp.status = :status WHERE gp.id = :postId")
    int updatePostStatus(@Param("postId") Long postId, @Param("status") PostStatus status);

}

