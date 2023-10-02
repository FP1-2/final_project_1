package com.facebook.service;

import com.facebook.dto.post.PostResponse;
import com.facebook.facade.PostFacade;
import com.facebook.model.AppUser;
import com.facebook.model.posts.Comment;
import com.facebook.model.posts.Like;
import com.facebook.model.posts.Post;
import com.facebook.model.posts.Repost;
import com.facebook.repository.posts.CommentRepository;
import com.facebook.repository.posts.LikeRepository;
import com.facebook.repository.posts.PostRepository;
import com.facebook.repository.posts.RepostRepository;
import com.facebook.utils.EX;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Сервісний клас, відповідальний за операції, пов'язані з публікаціями (Post),
 * отримання, збереження, видалення публікацій, та операції, такі як лайки,
 * коментування та репости.
 * <p>
 * Цей клас використовує {@link PostRepository}, {@link LikeRepository},
 * {@link CommentRepository}, {@link RepostRepository} .
 * </p>
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;

    private final RepostRepository repostRepository;

    private final PostFacade postFacade;

    //Тільки для генерації.
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    /**
     * Знаходить деталі публікацій користувача за його ID
     * із заданою сортуванням та пагінацією.
     *
     * <p>
     * Цей метод забезпечує можливість виводу деталізованої
     * інформації про публікації певного користувача,
     * задаючи критерії сортування та параметри пагінації.
     * Цей метод використовує {@link PostRepository}
     * для отримання даних та конвертує отримані результати
     * в DTO для подальшої передачі.
     * </p>
     *
     * @param userId ID користувача, публікації якого потрібно отримати.
     * @param page Номер сторінки для пагінації.
     * @param size Кількість елементів на одній сторінці.
     * @param sort Критерії сортування у форматі "property,direction"
     *             (наприклад, "date,desc").
     * @return Сторінка з деталізованою інформацією про публікації користувача.
     */
    public Page<PostResponse> findPostDetailsByUserId(Long userId,
                                                      int page,
                                                      int size,
                                                      String sort) {
        String[] sortParts = sort.split(",");
        String property = sortParts[0];

        String direction = sortParts.length > 1 ? sortParts[1] : "asc";

        Sort sorting = direction.equalsIgnoreCase("desc")
                ? Sort.by(Sort.Order.desc(property))
                : Sort.by(Sort.Order.asc(property));

        Pageable pageable = PageRequest.of(page, size, sorting);

        List<Map<String, Object>> results = postRepository
                .findPostDetailsByUserId(userId, pageable);

        //перевірити результат нативного запиту
        //results.forEach(EX::logMapKeysAndValues);

        //конвертуємо в DTO
        List<PostResponse> postResponses = results.stream()
                .map(postFacade::convertToPostResponse)
                .toList();

        Long totalElements = postRepository.countPostsByUserId(userId);

        return new PageImpl<>(postResponses, pageable, totalElements);
    }


    // Logic for liking a post
    public void likePost(AppUser user, Post post) {
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
    }

    // Logic for commenting on a post
    public void addComment(AppUser user, Post post, String commentText) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(commentText);
        commentRepository.save(comment);
    }

    // Logic for reposting a post
    public void repost(AppUser user, Post originalPost) {
        Repost repost = new Repost();
        repost.setUser(user);
        repost.setPost(originalPost);
        repostRepository.save(repost);
    }

}
