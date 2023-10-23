package com.facebook.repository.posts;


import com.facebook.model.AppUser;
import com.facebook.model.posts.Like;
import com.facebook.model.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(AppUser user, Post post);

    void deleteByPostId(Long postId);
}