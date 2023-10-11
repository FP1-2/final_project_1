package com.facebook.service;

import com.facebook.dto.post.ActionResponse;
import com.facebook.dto.post.CommentDTO;
import com.facebook.dto.post.CommentRequest;
import com.facebook.dto.post.CommentResponse;
import com.facebook.dto.post.PostRequest;
import com.facebook.dto.post.PostResponse;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.PostFacade;
import com.facebook.model.AbstractCreatedDate;
import com.facebook.model.AppUser;
import com.facebook.model.posts.Comment;
import com.facebook.model.posts.Like;
import com.facebook.model.posts.Post;
import com.facebook.model.posts.Repost;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.posts.CommentRepository;
import com.facebook.repository.posts.LikeRepository;
import com.facebook.repository.posts.PostRepository;
import com.facebook.repository.posts.RepostRepository;
import com.facebook.repository.posts.UserAndPostRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
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
 *
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;

    private final RepostRepository repostRepository;

    private final AppUserRepository appUserRepository;

    private final PostFacade postFacade;

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
     * Повертає об'єкт сортування на основі вхідного рядка сортування.
     *
     * @param sort рядок сортування у форматі "властивість, напрямок".
     *             Напрямок може бути або "asc", або "desc".
     *             Якщо напрямок відсутній,
     *             за замовчуванням вважається "asc".
     * @return об'єкт {@link Sort} для використання в запитах до репозиторію.
     */
    private Sort getSorting(String sort) {
        String[] sortParts = sort.split(",");
        String property = sortParts[0];

        String direction = sortParts.length > 1 ? sortParts[1] : "asc";

        return direction.equalsIgnoreCase("desc")
                ? Sort.by(Sort.Order.desc(property))
                : Sort.by(Sort.Order.asc(property));
    }

    /**
     * Повертає сторінку коментарів для заданого поста
     * з пагінацією та сортуванням.
     *
     * @param postId ID поста, для якого потрібно отримати коментарі.
     * @param page номер сторінки пагінації.
     * @param size розмір сторінки пагінації.
     * @param sort рядок сортування.
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
     *         у формі {@link PostResponse}.
     * @throws NotFoundException якщо пост з вказаним ID не знайдено.
     */
    public PostResponse findPostDetailsById(Long postId) {
        return postRepository.findPostDetailsById(postId)
                .filter(map -> map.get("id") != null)
                .map(postFacade::convertToPostResponse)
                .orElseThrow(() -> new NotFoundException("Post not found!"));
    }

    /**
     * Здійснює взаємодію користувача із постом (наприклад, лайк чи репост).
     * Якщо взаємодія вже існує, вона видаляється, інакше - створюється нова.
     *
     * @param userId Ідентифікатор користувача, який здійснює взаємодію.
     * @param postId Ідентифікатор поста, над яким здійснюється взаємодія.
     * @param repository Репозиторій для взаємодії (може бути для лайків, репостів тощо).
     * @param entitySupplier Функція для створення нової взаємодії.
     * @return Інформація про статус взаємодії: чи було додано чи видалено дію.
     */
    private <T extends AbstractCreatedDate> Optional<ActionResponse> handleAction(
            Long userId,
            Long postId,
            UserAndPostRepository<T, Long> repository,
            BiFunction<AppUser, Post, T> entitySupplier) {

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found!"));

        Optional<T> existingEntityOpt = repository.findByUserAndPost(user, post);

        return Optional.of(existingEntityOpt.map(existingEntity -> {
            repository.delete(existingEntity);
            return new ActionResponse(false, "Action removed");
        }).orElseGet(() -> {
            T newEntity = entitySupplier.apply(user, post);
            repository.save(newEntity);
            return new ActionResponse(true, "Action added");
        }));
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
    public Optional<ActionResponse> likePost(Long userId, Long postId) {
        return handleAction(userId, postId, likeRepository, (user, post) -> {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            return like;
        });
    }

    /**
     * Здійснює дію "репост" користувачем для поста.
     * Якщо репост вже існує, він видаляється, інакше - створюється новий репост.
     *
     * @param userId Ідентифікатор користувача, який робить репост.
     * @param postId Ідентифікатор поста, який репостять.
     * @return Інформація про статус репосту: чи було зроблено чи видалено репост.
     */
    public Optional<ActionResponse> repost(Long userId, Long postId) {
        return handleAction(userId, postId, repostRepository, (user, post) -> {
            Repost repost = new Repost();
            repost.setUser(user);
            repost.setPost(post);
            return repost;
        });
    }

    /**
     * Додає коментар до посту від імені користувача.
     *
     * @param userId Ідентифікатор користувача, який залишає коментар.
     * @param request Запит на додавання коментаря,
     *                який містить ідентифікатор поста та текст коментаря.
     * @return Інформація про доданий коментар у формі {@link CommentResponse}.
     */
    public Optional<CommentResponse> addComment(Long userId, CommentRequest request) {
        Post post = postRepository
                .findById(request.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found!"));

        return appUserRepository.findById(userId).map(user -> {
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setPost(post);
            comment.setContent(request.getContent());
            Comment savedComment = commentRepository.save(comment);
            return postFacade.convertToCommentResponse(savedComment);
        });
    }

    /**
     * Створює новий пост на основі запиту від користувача.
     *
     * @param request запит на створення поста,
     *                що містить всю необхідну інформацію для створення поста.
     * @param userId ідентифікатор користувача, який створює пост.
     * @return об'єкт {@link PostResponse} з даними створеного поста.
     * @throws NotFoundException якщо користувач або деталі поста не знайдені.
     */
    public PostResponse createPost(PostRequest request, Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        Post savedPost = postRepository
                .save(postFacade.convertPostRequestToPost(request, user));

        return postRepository.findPostDetailsById(savedPost.getId())
                .map(postFacade::convertToPostResponse)
                .orElseThrow(() -> new NotFoundException("Post details not found after creation!"));
    }

}
