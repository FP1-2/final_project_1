package com.facebook.repository.posts;

import com.facebook.dto.post.CommentDTO;
import com.facebook.model.posts.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для роботи з коментарями.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Знаходить усі коментарі для даного поста.
     *
     * @param postId   Ідентифікатор поста.
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

    /**
     * Здійснює пошук коментаря за ідентифікатором.
     *
     * @param commentId Ідентифікатор коментаря, який потрібно знайти.
     * @return Optional об'єкт CommentDTO, якщо коментар знайдено; інакше пустий Optional.
     */
    @Query("""
                SELECT new com.facebook.dto.post.CommentDTO(
                    c.id,
                    c.content,
                    c.createdDate,
                    new com.facebook.dto.post.Author(
                        c.user.id,
                        c.user.name,
                        c.user.surname,
                        c.user.username,
                        c.user.avatar
                    )
                )
                FROM Comment c
                WHERE c.id = :commentId
            """)
    Optional<CommentDTO> findCommentDtoById(@Param("commentId") Long commentId);

    void deleteByPostId(Long postId);

    List<Comment> findAllByPostIdIn(List<Long> postIds);
}

