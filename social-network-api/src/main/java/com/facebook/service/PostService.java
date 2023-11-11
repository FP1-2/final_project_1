package com.facebook.service;

import com.facebook.dto.post.ActionResponse;
import com.facebook.dto.post.CommentDTO;
import com.facebook.dto.post.CommentRequest;
import com.facebook.dto.post.CommentResponse;
import com.facebook.dto.post.PostPatchRequest;
import com.facebook.dto.post.PostRequest;
import com.facebook.dto.post.PostResponse;
import com.facebook.dto.post.RepostRequest;
import com.facebook.exception.NotFoundException;
import com.facebook.exception.UnauthorizedException;
import com.facebook.facade.PostFacade;
import com.facebook.model.AppUser;
import com.facebook.model.posts.Comment;
import com.facebook.model.posts.Like;
import com.facebook.model.posts.Post;
import com.facebook.model.posts.PostType;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.favorites.FavoriteRepository;
import com.facebook.repository.notifications.NotificationRepository;
import com.facebook.repository.posts.CommentRepository;
import com.facebook.repository.posts.LikeRepository;
import com.facebook.repository.posts.PostRepository;
import com.facebook.service.notifications.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.facebook.utils.SortUtils.getSorting;


/**
 * Сервісний клас, відповідальний за операції, пов'язані з публікаціями (Post),
 * отримання, збереження, видалення публікацій, та операції, такі як лайки,
 * коментування та репости.
 * <p>
 * Цей клас використовує {@link PostRepository}, {@link LikeRepository},
 * {@link CommentRepository}.
 * </p>
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PostService {

    private static final String USER_NOT_FOUND = "User not found!";

    private static final String POST_NOT_FOUND = "Post not found!";

    private final PostRepository postRepository;

    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;

    private final AppUserRepository appUserRepository;

    private final PostFacade postFacade;

    private final NotificationRepository notificationRepository;

    private final NotificationService notificationService;

    private final FavoriteRepository favoriteRepository;

    /**
     * Отримує усі пости з бази даних.
     * <p>
     * Використовується для генерації даних.
     * </p>
     *
     * @return Список усіх постів.
     */
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    /**
     * Зберігає пост у базі даних.
     * <p>
     * Використовується для генерації даних.
     * </p>
     *
     * @param post Об'єкт поста, який потрібно зберегти.
     */
    public void save(Post post) {
        postRepository.save(post);
    }

    /**
     * Повертає сторінку коментарів для заданого поста
     * з пагінацією та сортуванням.
     *
     * @param postId ID поста, для якого потрібно отримати коментарі.
     * @param page   номер сторінки пагінації.
     * @param size   розмір сторінки пагінації.
     * @param sort   рядок сортування.
     * @return сторінка коментарів у форматі DTO.
     */
    public Page<CommentDTO> getCommentsByPostId(Long postId,
                                                int page,
                                                int size,
                                                String sort) {
        Sort sorting = getSorting(sort);
        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        return comments.map(postFacade::convertToCommentDTO);
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
     * @param page   Номер сторінки для пагінації.
     * @param size   Кількість елементів на одній сторінці.
     * @param sort   Критерії сортування у форматі "property,direction"
     *               (наприклад, "date,desc").
     * @return Сторінка з деталізованою інформацією про публікації користувача.
     */
    public Page<PostResponse> findPostDetailsByUserId(Long userId,
                                                      int page,
                                                      int size,
                                                      String sort) {
        Sort sorting = getSorting(sort);
        Pageable pageable = PageRequest.of(page, size, sorting);

        List<Map<String, Object>> results = postRepository
                .findPostDetailsByUserId(userId, pageable);

        List<PostResponse> postResponses = results
                .stream().map(postFacade::convertToPostResponse).toList();
        Long totalElements = postRepository.countPostsByUserId(userId);

        return new PageImpl<>(postResponses, pageable, totalElements);
    }

    /**
     * Отримує інформацію про пост за його ID.
     * <p>
     * Якщо пост з вказаним ID не знайдено або відсутній ID у відповіді репозиторію,
     * метод генерує виключення {@link NotFoundException}.
     * </p>
     *
     * @param postId ID поста, деталі якого потрібно отримати.
     * @return Деталізована інформація про пост
     * у формі {@link PostResponse}.
     * @throws NotFoundException якщо пост з вказаним ID не знайдено.
     */
    public PostResponse findPostDetailsById(Long postId) {
        return postRepository.findPostDetailsById(postId)
                .filter(map -> map.get("post_id") != null)
                .map(postFacade::convertToPostResponse)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));
    }

    /**
     * Здійснює дію "лайк" користувачем для поста.
     * Якщо лайк вже існує, він видаляється,
     * інакше - створюється новий лайк.
     *
     * @param userId Ідентифікатор користувача, який ставить лайк.
     * @param postId Ідентифікатор поста, якому ставлять лайк.
     * @return Інформація про статус лайку: чи було додано чи видалено лайк.
     */
    public Optional<ActionResponse> likePost(Long userId,
                                             Long postId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        return Optional.of(existingLike
                .map(like -> {
                    likeRepository.delete(like);
                    return new ActionResponse(false, "Like removed");
                })
                .orElseGet(() -> {
                    Like like = new Like();
                    like.setUser(user);
                    like.setPost(post);
                    likeRepository.save(like);

                    if(!Objects.equals(user.getId(), post.getUser().getId())) notificationService.createLikeNotification(user, post);

                    return new ActionResponse(true, "Like added");
                }));
    }

    /**
     * Додає коментар до посту від імені користувача.
     *
     * @param userId  Ідентифікатор користувача, який залишає коментар.
     * @param request Запит на додавання коментаря,
     *                який містить ідентифікатор поста та текст коментаря.
     * @return Інформація про доданий коментар у формі {@link CommentResponse}.
     */
    public Optional<CommentResponse> addComment(Long userId,
                                                CommentRequest request) {
        Post post = postRepository
                .findById(request.getPostId())
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));

        return appUserRepository.findById(userId).map(user -> {
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setPost(post);
            comment.setContent(request.getContent());
            Comment savedComment = commentRepository.save(comment);

            if(!Objects.equals(user.getId(), post.getUser().getId())) notificationService.createCommentNotification(user, post);

            return postFacade.convertToCommentResponse(savedComment);
        });
    }

    /**
     * Виконує каскадне видалення поста за його ідентифікатором
     * та всіх пов'язаних з ним записів (лайків, коментарів).
     *
     * @param postId ідентифікатор поста, який потрібно
     *               видалити разом зі своїми залежностями
     */
    public void performCascadeDeletion(Long postId) {

        likeRepository.deleteByPostId(postId);

        commentRepository.deleteByPostId(postId);

        notificationRepository.deleteByPostId(postId);

        postRepository.deleteById(postId);
    }

    /**
     * Виконує дію репосту допису користувачем. Логіка роботи наступна:
     *
     * <ul>
     *     <li>Якщо репостимо пост і репоста на пост немає: створюємо новий репост.</li>
     *     <li>Якщо репостимо пост і репост вже наш є:
     *         видаляємо існуючий репост.</li>
     *     <li>Якщо репостимо репост: визначаємо оригінальний пост та репостимо його.</li>
     *     <li>Якщо репостимо репост і оригінал нами репостився:
     *         видаляємо репост оригінального допису.</li>
     * </ul>
     *
     * @param request Запит на репост, який містить інформацію про допис, який необхідно репостити.
     * @param userId Ідентифікатор користувача, який ініціює репост.
     * @return Відповідь з деталями про створений або видалений репост.
     */
    @Transactional
    public Optional<ActionResponse> createRepost(RepostRequest request,
                                                 Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        Post originalPost = postRepository.findById(request.getOriginalPostId())
                .orElseThrow(() -> new NotFoundException("Original post not found!"));

        Long originalPostId = Optional.of(originalPost)
                .filter(p -> p.getType() == PostType.REPOST && p.getOriginalPostId() != null)
                .map(Post::getOriginalPostId)
                .orElse(request.getOriginalPostId());

        Optional<Post> existingRepost = postRepository
                .findByUserAndOriginalPostId(user, originalPostId);

        return Optional.of(existingRepost
                .map(repost -> {
                    performCascadeDeletion(repost.getId());
                    return new ActionResponse(false, "Repost removed");
                })
                .orElseGet(() -> {
                    Post repost = postFacade.convertRepostRequestToPost(request, user);
                    postRepository.save(repost);

                    notificationService.createRepostNotification(user, repost, originalPost.getUser());

                    return new ActionResponse(true, "Repost added");
                }));
    }

    /**
     * Створює новий пост на основі запиту від користувача.
     *
     * @param request запит на створення поста,
     *                що містить всю необхідну інформацію для створення поста.
     * @param userId  ідентифікатор користувача, який створює пост.
     * @return об'єкт {@link PostResponse} з даними створеного поста.
     * @throws NotFoundException якщо користувач або деталі поста не знайдені.
     */
    public PostResponse createPost(PostRequest request, Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        Post savedPost = postRepository
                .save(postFacade.convertPostRequestToPost(request, user));

        notificationService.createFriendPostNotification(user, savedPost);

        return postRepository.findPostDetailsById(savedPost.getId())
                .map(postFacade::convertToPostResponse)
                .orElseThrow(() -> new NotFoundException("Post details not found after creation!"));
    }

    /**
     * Оновлює існуючий пост/репост за допомогою наданого запиту на оновлення.
     *
     * @param request DTO, що містить поля для оновлення поста.
     * @param postId  ідентифікатор поста, який потрібно оновити.
     * @param userId  ідентифікатор користувача, який робить запит на оновлення.
     * @return об'єкт {@link PostResponse} з даними оновленого поста.
     * @throws NotFoundException     якщо пост або користувач не знайдені.
     * @throws UnauthorizedException якщо користувач не має прав на оновлення цього поста.
     */
    public PostResponse updatePost(PostPatchRequest request, Long postId, Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));

        Optional.of(user.getId())
                .filter(id -> id.equals(existingPost.getUser().getId()))
                .orElseThrow(() -> new UnauthorizedException("User not authorized to update this post!"));

        Optional
                .ofNullable(request.getImageUrl())
                .ifPresent(existingPost::setImageUrl);
        Optional
                .ofNullable(request.getTitle())
                .ifPresent(existingPost::setTitle);
        Optional
                .ofNullable(request.getBody())
                .ifPresent(existingPost::setBody);

        Post savedPost = postRepository.save(existingPost);

        return postRepository.findPostDetailsById(savedPost.getId())
                .map(postFacade::convertToPostResponse)
                .orElseThrow(() -> new NotFoundException("Post details not found after update!"));
    }

    /**
     * Знаходить усі пости з заданою пагінацією та сортуванням.
     *
     * @param page Номер сторінки для пагінації.
     * @param size Розмір сторінки для пагінації.
     * @param sort Строка сортування.
     * @return Сторінка з постами.
     */
    public Page<PostResponse> findAllPosts(int page, int size, String sort) {
        Sort sorting = getSorting(sort);
        Pageable pageable = PageRequest.of(page, size, sorting);

        List<Map<String, Object>> results = postRepository.findAllPostDetails(pageable);

        List<PostResponse> postResponses = results
                .stream().map(postFacade::convertToPostResponse).toList();
        Long totalElements = postRepository.countAllPosts();

        return new PageImpl<>(postResponses, pageable, totalElements);
    }

    public void performRepostCascadeDeletion(Long postId) {
        List<Post> repostRepo = postRepository.findByOriginalPostId(postId);
        List<Long> repostIds = repostRepo.stream().map(Post::getId).toList();

        Optional.of(notificationRepository.findAllByPostIdIn(repostIds))
                .filter(notifications -> !notifications.isEmpty())
                .ifPresent(notificationRepository::deleteAllInBatch);

        Optional.of(postRepository.findByOriginalPostId(postId))
                .filter(reposts -> !reposts.isEmpty())
                .ifPresent(postRepository::deleteAllInBatch);

        Optional.of(commentRepository.findAllByPostIdIn(repostIds))
                .filter(comments -> !comments.isEmpty())
                .ifPresent(commentRepository::deleteAllInBatch);

        Optional.of(likeRepository.findAllByPostIdIn(repostIds))
                .filter(likes -> !likes.isEmpty())
                .ifPresent(likeRepository::deleteAllInBatch);

        Optional.of(favoriteRepository.findAllByPostIdIn(repostIds))
                .filter(favorites -> !favorites.isEmpty())
                .ifPresent(favoriteRepository::deleteAllInBatch);
    }

    public void performAllCascadeDeletion(Long postId) {
        favoriteRepository.deleteByPostId(postId);
        performCascadeDeletion(postId);
        performRepostCascadeDeletion(postId);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post existedPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));

        if(userId.equals(existedPost.getUser().getId())) {
            performAllCascadeDeletion(postId);
        } else {
            throw new UnauthorizedException("User not authorised to delete this post!");
        }
    }

}
