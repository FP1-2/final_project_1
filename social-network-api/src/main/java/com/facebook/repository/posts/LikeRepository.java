package com.facebook.repository.posts;


import com.facebook.model.AppUser;
import com.facebook.model.posts.Like;
import com.facebook.model.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(AppUser user, Post post);

    void deleteByPostId(Long postId);

    List<Like> findAllByPostIdIn(List<Long> postIds);

    /**
     * Перевіряє, чи існує лайк від конкретного користувача на конкретний пост.
     *
     * @param postId Ідентифікатор поста.
     * @param userId Ідентифікатор користувача.
     * @return true, якщо лайк від користувача на пост існує.
     */
    boolean existsByPostIdAndUserId(Long postId, Long userId);
}