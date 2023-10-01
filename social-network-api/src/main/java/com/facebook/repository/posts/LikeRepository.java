package com.facebook.repository.posts;

import com.facebook.model.posts.Like;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // Отримати всі лайки для конкретного посту.
    List<Like> findByPostId(Long postId);
}