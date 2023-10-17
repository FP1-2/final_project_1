package com.facebook.repository.posts;

import com.facebook.model.posts.Repost;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepostRepository extends UserAndPostRepository<Repost, Long> {
    // Отримати всі репости певного користувача.
    List<Repost> findByUserId(Long userId);

}
