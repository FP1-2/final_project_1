package com.facebook.repository.posts;

import com.facebook.model.posts.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторій для роботи з коментарями.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Знаходить усі коментарі для даного поста.
     *
     * @param postId Ідентифікатор поста.
     * @param pageable Параметри пагінації.
     * @return Сторінка з коментарями.
     */
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    /**
     * Підраховує кількість коментарів для даного поста.
     *
     * @param postId Ідентифікатор поста.
     * @return Кількість коментарів.
     */
    Long countByPostId(Long postId);

    void deleteByPostId(Long postId);

}

