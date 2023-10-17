package com.facebook.repository.posts;

import com.facebook.model.AppUser;
import com.facebook.model.posts.Post;
import com.facebook.model.posts.Repost;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepostRepository extends JpaRepository<Repost, Long> {
    // Отримати всі репости певного користувача.
    List<Repost> findByUserId(Long userId);
    Optional<Repost> findByUserAndPost(AppUser user, Post post);
}
