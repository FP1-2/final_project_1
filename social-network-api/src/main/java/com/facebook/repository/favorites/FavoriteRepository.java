package com.facebook.repository.favorites;

import com.facebook.model.favorites.Favorite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    String FAVORITE_POST_DETAILS_SELECT = """
                        SELECT
                            p.id AS post_id,
                            p.created_date,
                            p.last_modified_date,
                            p.image_url,
                            p.title,
                            p.body,
                            p.status,
                            p.type,
                            p.original_post_id,
                          
                            u.id AS user_id,
                            u.name,
                            u.surname,
                            u.username,
                            u.avatar,
                          
                            GROUP_CONCAT(DISTINCT c.id) AS comment_ids,
                            GROUP_CONCAT(DISTINCT l.id) AS like_ids,
                            GROUP_CONCAT(DISTINCT r.id) AS repost_ids,
                           
                            ou.id AS original_user_id,
                            ou.name AS original_name,
                            ou.surname AS original_surname,
                            ou.username AS original_username,
                            ou.avatar AS original_avatar,
                          
                            op.title AS original_title,
                            op.body AS original_body,
                            op.status AS original_status,
                            op.type AS original_type,
                          
                            GROUP_CONCAT(DISTINCT oc.id) AS original_comment_ids,
                            GROUP_CONCAT(DISTINCT ol.id) AS original_like_ids,
                            GROUP_CONCAT(DISTINCT orp.id) AS original_repost_ids
                        FROM
                            favorites f
                        JOIN posts p ON f.post_id = p.id
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

    @Query(value = FAVORITE_POST_DETAILS_SELECT + """
             WHERE f.user_id = :userId
             GROUP BY p.id, u.id, op.id, ou.id
            """,
            countQuery = "SELECT count(*) FROM favorites WHERE user_id = :userId",
            nativeQuery = true)
    List<Map<String, Object>> findFavoritePostDetailsByUserId(@Param("userId") Long userId,
                                                              Pageable pageable);

    @Query(value = "SELECT count(*) FROM favorites WHERE user_id = :userId",
            nativeQuery = true)
    Long countFavoritesByUserId(@Param("userId") Long userId);

    Optional<Favorite> findByUserIdAndPostId(Long userId, Long postId);

    List<Favorite> findAllByPostIdIn(List<Long> postIds);

    void deleteByPostId(Long postId);

}
