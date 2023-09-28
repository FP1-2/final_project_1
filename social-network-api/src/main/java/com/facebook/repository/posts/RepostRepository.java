package com.facebook.repository.posts;

import com.facebook.model.AppUser;
import com.facebook.model.posts.Repost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepostRepository extends JpaRepository<Repost, Long> {
    // Отримати всі репости певного користувача.
    List<Repost> findByUser(AppUser user);
}
