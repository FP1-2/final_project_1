package com.facebook.repository.posts;

import com.facebook.model.AppUser;
import com.facebook.model.posts.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // Отримати всі лайки певного користувача.
    List<Like> findByUser(AppUser user);
}
