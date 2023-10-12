package com.facebook.repository.posts;

import com.facebook.model.AppUser;
import com.facebook.model.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface UserAndPostRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findByUserAndPost(AppUser user, Post post);

}
