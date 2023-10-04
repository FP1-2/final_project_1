package com.facebook.repository.posts;

import com.facebook.model.posts.Repost;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepostRepository extends JpaRepository<Repost, Long> {
    // Отримати всі репости певного користувача.
    List<Repost> findByUserId(Long userId);
}
