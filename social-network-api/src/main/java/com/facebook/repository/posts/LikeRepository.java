package com.facebook.repository.posts;

import com.facebook.model.posts.Like;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends UserAndPostRepository<Like, Long> {
    // Отримати всі лайки для конкретного посту.
    List<Like> findByPostId(Long postId);

}