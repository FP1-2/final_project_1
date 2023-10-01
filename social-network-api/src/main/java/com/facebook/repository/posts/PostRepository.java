package com.facebook.repository.posts;

import com.facebook.model.posts.Post;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
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
     * @param userId    Ідентифікатор користувача для якого потрібно знайти деталі постів.
     * @param pageable  Об'єкт, що містить інформацію про сторінковість
     *                  (наприклад, номер сторінки та розмір сторінки).
     * @return Список мап, де кожна мапа представляє детальну інформацію про пост.
     */
    @Query(value = """
            SELECT
                p.id, p.created_date, p.last_modified_date, p.title, p.body, p.status,
                u.id as user_id, u.name, u.surname, u.username, u.avatar,
                CAST(GROUP_CONCAT(DISTINCT c.id) AS CHAR) AS comment_ids,
                CAST(GROUP_CONCAT(DISTINCT l.id) AS CHAR) AS like_ids,
                CAST(GROUP_CONCAT(DISTINCT r.id) AS CHAR) AS repost_ids
            FROM
                posts p
            LEFT JOIN
                users u ON p.user_id = u.id
            LEFT JOIN
                comments c ON p.id = c.post_id
            LEFT JOIN
                likes l ON p.id = l.post_id
            LEFT JOIN
                reposts r ON p.id = r.post_id
            WHERE
                p.user_id = :userId
            GROUP BY
                p.id
            """,
            countQuery = "SELECT count(*) FROM posts WHERE user_id = :userId",
            nativeQuery = true)
    List<Map<String, Object>> findPostDetailsByUserId(@Param("userId")
                                                      Long userId,
                                                      Pageable pageable);

    /**
     * Отримує загальну кількість постів для конкретного користувача в базі даних.
     *
     * @param userId Ідентифікатор користувача.
     * @return Кількість постів, які належать даному користувачу.
     */
    @Query(value = "SELECT count(*) FROM posts WHERE user_id = :userId", nativeQuery = true)
    Long countPostsByUserId(@Param("userId") Long userId);
}

