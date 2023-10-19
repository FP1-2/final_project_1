package com.facebook.controller;

import com.facebook.dto.post.ActionResponse;
import com.facebook.dto.post.CommentDTO;
import com.facebook.dto.post.CommentRequest;
import com.facebook.dto.post.CommentResponse;
import com.facebook.dto.post.PostRequest;
import com.facebook.dto.post.PostResponse;
import com.facebook.dto.post.RepostRequest;
import com.facebook.service.CurrentUserService;
import com.facebook.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Контролер для обробки запитів, пов'язаних з постами.
 * Надає засоби для додавання коментарів,
 * лайків, репостів, а також для отримання та створення постів.
 */
@Log4j2
@Validated
@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final CurrentUserService currentUserService;

    /**
     * Повертає всі коментарі для заданого поста з пагінацією та сортуванням.
     *
     * @param postId ID поста, для якого потрібно отримати коментарі.
     * @param page Номер сторінки пагінації.
     * @param size Розмір сторінки пагінації.
     * @param sort Параметр для вказівки властивості
     *             та напрямку сортування (напр. "createdDate,desc").
     * @return сторінка з коментарями в форматі DTO.
     *
     * <p>
     * <strong>Приклад:</strong>
     *  /api/posts/4/comments?page=0&size=10&sort=createdDate,desc
     * </p>
     */
    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentDTO>> getCommentsByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        return ResponseEntity.ok(postService.getCommentsByPostId(postId, page, size, sort));
    }

    /**
     * Додає "лайк" до поста з заданим ID від поточного користувача.
     *
     * @param postId Ідентифікатор поста, який потрібно "лайкнути".
     * @return Відгук про дію.
     *
     * <p>
     * <strong>Приклад:</strong>
     *     /api/posts/like/4
     * </p>
     */
    @PostMapping("/like/{postId}")
    public ResponseEntity<ActionResponse> likePost(@PathVariable Long postId) {
        Long userId = currentUserService.getCurrentUserId();
        return postService.likePost(userId, postId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Додає коментар до поста від поточного користувача.
     *
     * @param commentRequest Запит на створення коментаря.
     * @return Відповідь з даними нового коментаря.
     * <p>
     * <strong>Приклад:</strong>
     * /api/posts/comment
     * </p>
     * <p>
     * <strong>Тіло запиту:</strong>
     * { "postId": 4, "commentText": "Great post!" }
     * </p>
     */
    @PostMapping("/comment")
    public ResponseEntity<CommentResponse> addComment(@Validated
                                                      @RequestBody
                                                      CommentRequest commentRequest) {
        Long userId = currentUserService.getCurrentUserId();
        return postService.addComment(userId, commentRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    /**
     * Отримує всі пости користувача з заданим ID.
     *
     * @param userId Ідентифікатор користувача, пости якого потрібно отримати.
     * @param page   Номер сторінки.
     * @param size   Розмір сторінки.
     * @param sort   Параметри сортування.
     * @return Сторінка з постами користувача.
     *
     * <p>
     * <strong>Приклад:</strong>
     *   /api/posts/by_user_id/1?page=0&size=10&sort=id,desc
     * </p>
     */
    @GetMapping("/by_user_id/{userId}")
    public ResponseEntity<Page<PostResponse>> getPostsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        Page<PostResponse> postResponses = postService
                .findPostDetailsByUserId(userId, page, size, sort);
        return ResponseEntity.ok(postResponses);
    }

    /**
     * Отримує деталі поста за його ID.
     *
     * @param postId Ідентифікатор поста.
     * @return Деталі поста.
     * <p>
     * <strong>Приклад:</strong>
     *    /api/posts/4
     * </p>
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId) {
        PostResponse postResponse = postService.findPostDetailsById(postId);
        return ResponseEntity.ok(postResponse);
    }

    /**
     * Створює новий пост.
     *
     * @param postRequest DTO з даними для створення поста.
     * @return DTO новоствореного поста.
     * <p>
     * <strong>Приклад:</strong>
     * /api/posts/
     * </p>
     * <p>
     * <strong>Тіло запиту:</strong>
     * {"imageUrl": "some-image-url.jpg", "title": "Це мій новий пост",
     *  "body": "Тут міститься вміст мого поста."}
     * </p>
     */
    @PostMapping("/post")
    public ResponseEntity<PostResponse> createPost(@Validated
                                                   @RequestBody
                                                   PostRequest postRequest) {
        Long userId = currentUserService.getCurrentUserId();
        PostResponse postResponse = postService.createPost(postRequest, userId);
        return ResponseEntity.ok(postResponse);
    }

    /**
     * Обробляє запит на репост поста.
     * <p>
     * Цей метод використовує логіку, описану в {@link PostService#createRepost}.
     * </p>
     * <p>
     * Додатково, цей метод використовує валідацію вхідних даних за допомогою моделі
     * {@link RepostRequest}.
     * </p>
     *
     * @param repostRequest Об'єкт запиту на репост, який містить інформацію про допис, який необхідно репостити.
     * @return {@code ResponseEntity<ActionResponse>} Якщо репост був успішно створено чи видалено,
     *         повертає статус OK (200) з відповіддю. В іншому випадку повертає статус Not Found (404).
     */
    @PostMapping("/repost")
    public ResponseEntity<ActionResponse> createRepost(@Validated
                                                       @RequestBody
                                                       RepostRequest repostRequest) {
        Long userId = currentUserService.getCurrentUserId();
        return postService.createRepost(repostRequest, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
