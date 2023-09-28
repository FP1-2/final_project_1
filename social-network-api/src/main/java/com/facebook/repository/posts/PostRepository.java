package com.facebook.repository.posts;

import com.facebook.model.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
