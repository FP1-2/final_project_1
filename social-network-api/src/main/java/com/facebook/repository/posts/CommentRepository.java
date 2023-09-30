package com.facebook.repository.posts;

import com.facebook.model.posts.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Отримати всі коментарі до певного посту.
    List<Comment> findByPostId(Long postId);
}
