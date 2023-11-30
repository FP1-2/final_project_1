package com.facebook.repository.posts;

import com.facebook.model.AppUser;
import com.facebook.model.posts.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Репозиторій для роботи з постами.
 * <p>
 * Надає методи для доступу до даних постів у базі даних,
 * таких як отримання постів за певними критеріями,
 * підрахунок кількості постів
 * та інші додаткові операції.
 * </p>
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByUserAndOriginalPostId(AppUser user, Long originalPostId);

    /**
     * SQL-запит для витягу детальної інформації по постах.
     * <p>
     * Запит дозволяє отримати:
     * - основну інформацію по посту (ID, дати створення та модифікації,
     * URL зображення, заголовок, тіло, статус, тип);
     * - дані користувача, який опублікував пост (ID, ім'я, прізвище,
     * ім'я користувача, аватар);
     * - списки ID коментарів, лайків та репостів, які відносяться до посту.
     * </p>
     * <p>
     * Додатково, якщо даний пост є репостом іншого посту, запит також отримує
     * інформацію про оригінальний пост та користувача, який створив
     * оригінальний пост.
     * </p>
     * <p>
     * Цей запит використовується в двох основних методах:
     * - {@link #findPostDetailsByUserId(Long, Pageable)}:
     * для отримання деталей усіх постів певного користувача;
     * - {@link #findPostDetailsById(Long, Long)}:
     * для отримання деталей конкретного посту за його ID.
     * </p>
     * <p>
     * Зверніть увагу, що ця константа містить базову частину запиту,
     * яка пізніше розширюється додатковими умовами в залежності від конкретного методу,
     * в якому вона використовується.
     * </p>
     */
    String POST_DETAILS_SELECT = """
                        SELECT
                            p.id AS post_id,
                            p.created_date,
                            p.last_modified_date,
                            p.image_url,
                            p.title,
                            p.body,
                            p.type,
                            p.original_post_id,
                          
                            u.id AS user_id,
                            u.name,
                            u.surname,
                            u.username,
                            u.avatar,
                          
                            GROUP_CONCAT(DISTINCT c.id) AS comment_ids,
                            (SELECT
                                 GROUP_CONCAT(l.user_id)
                                 FROM likes l WHERE l.post_id = p.id) AS like_user_ids,
                            GROUP_CONCAT(DISTINCT r.id) AS repost_ids,
                           
                            ou.id AS original_user_id,
                            ou.name AS original_name,
                            ou.surname AS original_surname,
                            ou.username AS original_username,
                            ou.avatar AS original_avatar,
                          
                            op.title AS original_title,
                            op.body AS original_body,
                            op.type AS original_type,
                          
                            GROUP_CONCAT(DISTINCT oc.id) AS original_comment_ids,
                            (SELECT
                                 GROUP_CONCAT(ol.user_id)
                                 FROM likes ol WHERE ol.post_id = op.id) AS original_like_user_ids,
                            GROUP_CONCAT(DISTINCT orp.id) AS original_repost_ids,
                            (SELECT COUNT(*)
                             FROM favorites f
                             WHERE f.post_id = p.id AND f.user_id = :userId) > 0 AS is_favorite
                             
                        FROM
                            posts p
                        LEFT JOIN posts op ON p.original_post_id = op.id
                        LEFT JOIN users u ON p.user_id = u.id
                        LEFT JOIN users ou ON op.user_id = ou.id
                        
                        LEFT JOIN comments c ON p.id = c.post_id
                        LEFT JOIN comments oc ON op.id = oc.post_id
                        
                        LEFT JOIN likes l ON p.id = l.post_id
                        LEFT JOIN likes ol ON op.id = ol.post_id
                        
                        LEFT JOIN posts orp ON op.id = orp.original_post_id AND orp.type = 'REPOST'
                        LEFT JOIN posts r ON p.id = r.original_post_id AND r.type = 'REPOST'
            """;

    /**
     * Знаходить деталі постів за ідентифікатором користувача.
     * <p>
     * Даний метод використовує нативний SQL-запит для отримання детальної інформації
     * про пости конкретного користувача.
     * Інформація включає основні деталі поста, дані користувача, який створив пост,
     * а також списки ID коментарів, лайків та репостів,
     * які відносяться до цього поста.
     * </p>
     *
     * @param userId   Ідентифікатор користувача для якого потрібно знайти деталі постів.
     * @param pageable Об'єкт, що містить інформацію про сторінковість
     *                 (наприклад, номер сторінки та розмір сторінки).
     * @return Список мап, де кожна мапа представляє детальну інформацію про пост.
     */
    @Query(value = POST_DETAILS_SELECT + """
             WHERE p.post_type = 'Post' AND p.user_id = :userId
             GROUP BY p.id, u.id, op.id, ou.id
            """,
            countQuery = """
                    SELECT count(*) FROM posts p
                    WHERE p.post_type = 'Post' AND user_id = :userId
                    """,
            nativeQuery = true)
    List<Map<String, Object>> findPostDetailsByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * Знаходить деталі конкретного посту за його ідентифікатором.
     * <p>
     * Метод використовує нативний SQL-запит для отримання інформації
     * про пост за його ID.
     * Інформація включає дані користувача, який створив пост,
     * а також списки ID коментарів, лайків та репостів,
     * які відносяться до цього поста.
     * </p>
     *
     * @param postId Ідентифікатор посту, деталі якого потрібно отримати.
     * @return Мапа, що представляє детальну інформацію про пост.
     */
    @Query(value = POST_DETAILS_SELECT + """
            WHERE
                p.post_type = 'Post' AND p.id = :postId
            GROUP BY p.id, u.id, op.id, ou.id
            """, nativeQuery = true)
    Optional<Map<String, Object>> findPostDetailsById(@Param("userId") Long userId,
                                                      @Param("postId") Long postId);


    /**
     * Отримує загальну кількість постів для конкретного користувача в базі даних.
     *
     * @param userId Ідентифікатор користувача.
     * @return Кількість постів, які належать даному користувачу.
     */
    @Query(value = """
            SELECT count(*) FROM posts p
            WHERE p.post_type = 'Post' AND user_id = :userId
            """, nativeQuery = true)
    Long countPostsByUserId(@Param("userId") Long userId);

    /**
     * Знаходить сторінку постів, кожен з яких має більше ніж 4 коментарі.
     * <p>
     * Метод використовує JPQL-запит для пошуку постів, до кожного з яких додано понад 4 коментарі.
     * Повертається сторінка результатів згідно з параметрами пагінації.
     * Якщо пости відповідно до умов запиту не знайдені, повертається пуста сторінка.
     * </p>
     *
     * @param pageable параметри пагінації, які визначають, які пости та скільки постів будуть повернуті.
     * @return Сторінка об'єктів Post, яка може містити знайдені пости або бути пустою.
     */
    @Query("""
            SELECT p
            FROM Post p
            WHERE TYPE(p) = Post
            AND (SELECT COUNT(c.id)
                   FROM Comment c
                   WHERE c.post = p) > 4
            """)
    Page<Post> findFirstPostWithMoreThanFourComments(Pageable pageable);

    /**
     * Виконує запит до бази даних для отримання деталей усіх постів з пагінацією.
     *
     * @param pageable Параметри пагінації та сортування.
     * @return Список постів з деталями.
     */
    @Query(value = POST_DETAILS_SELECT + """
             WHERE p.post_type = 'Post'
             GROUP BY p.id, u.id, op.id, ou.id
            """,
            countQuery = """
                    SELECT count(*) FROM posts p
                    WHERE p.post_type = 'Post'
                    """,
            nativeQuery = true)
    List<Map<String, Object>> findAllPostDetails(@Param("userId") Long userId,
                                                 Pageable pageable);

    /**
     * Підраховує загальну кількість постів у базі даних.
     *
     * @return Загальна кількість постів.
     */
    @Query(value = """
            SELECT count(*) FROM posts p
            WHERE p.post_type = 'Post'
            """,
            nativeQuery = true)
    Long countAllPosts();

    /**
     * Запит для отримання типу поста на основі його ідентифікатора.
     * <p>
     * Цей метод виконує нативний SQL запит до бази даних для отримання значення поля
     * {@code post_type} з таблиці {@code posts} для конкретного поста, ідентифікатор якого передається в метод.
     * Тип поста використовується для ідентифікації конкретного класу або категорії постів.
     * <p>
     * @param postId Ідентифікатор поста, для якого потрібно отримати тип.
     * @return {@code Optional<String>} з типом поста. Якщо пост з вказаним ідентифікатором не знайдено,
     *         повертається порожній {@code Optional}.
     */
    @Query(value = """
            SELECT post_type
            FROM posts
            WHERE id = :postId
            """,
            nativeQuery = true)
    Optional<String> findPostTypeById(@Param("postId") Long postId);

    List<Post> findByOriginalPostId(Long originalPostId);

}
