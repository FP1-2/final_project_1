package com.facebook.repository.posts;

import com.facebook.model.posts.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Отримати всі коментарі до певного посту.
    List<Comment> findByPostId(Long postId);
}
