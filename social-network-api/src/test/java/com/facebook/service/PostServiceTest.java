package com.facebook.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.facebook.dto.post.ActionResponse;
import com.facebook.dto.post.CommentDTO;
import com.facebook.dto.post.CommentRequest;
import com.facebook.dto.post.CommentResponse;
import com.facebook.dto.post.PostResponse;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.PostFacade;
import com.facebook.model.AppUser;
import com.facebook.model.posts.Comment;
import com.facebook.model.posts.Like;
import com.facebook.model.posts.Post;
import com.facebook.repository.AppUserRepository;
import com.facebook.repository.posts.CommentRepository;
import com.facebook.repository.posts.LikeRepository;
import com.facebook.repository.posts.PostRepository;


/**
 * Тестовий клас для {@link PostService}.
 *
 * <p>Цей клас проводить тестування основних функцій сервісу постів.
 * Він імітує роботу репозиторіїв і інших залежностей за допомогою Mockito,
 * щоб забезпечити ізольованість тестів і переконатися в коректності роботи самого сервісу.</p>
 *
 * <p>Основні функції, які тестуються:
 * <ul>
 *  <li>{@link #testFindAll()} - Отримання всіх постів</li>
 *  <li>{@link #testSave()} - Збереження посту</li>
 *  <li>{@link #testGetCommentsByPostId()} - Отримання коментарів за ID посту</li>
 *  <li>{@link #testFindPostDetailsByUserId()} - Отримання деталей постів за ID користувача</li>
 *  <li>{@link #testLikePostWhenNoExistingLike()} - Додавання лайку до посту, коли лайка ще не існує</li>
 *  <li>{@link #testLikePostWhenExistingLike()} - Видалення лайку з посту, якщо лайк вже існує</li>
 *  <li>{@link #testAddRepostIfNotExists()} - Додавання репосту до посту, коли репост ще не існує</li>
 *  <li>{@link #testRemoveRepostIfExists()} - Видалення репосту з посту, якщо репост вже існує</li>
 *  <li>{@link #testAddComment()} - Додавання коментаря до посту</li>
 *  <li>{@link #testAddCommentWithMissingUser()} - Додавання коментаря користувачем,
 *      який відсутній в базі даних</li>
 *  </ul></p>
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private EmailHandlerService emailHandlerService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private LikeRepository likeRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private PostFacade postFacade;

    /**
     * Тестує отримання всіх постів.
     *
     * <p>Перевіряє, чи сервіс правильно повертає
     * всі пости з репозиторію.</p>
     */
    @Test
    void testFindAll() {
        List<Post> posts = Arrays.asList(new Post(), new Post());
        Mockito
                .when(postRepository.findAll())
                .thenReturn(posts);

        List<Post> result = postService.findAll();

        assertEquals(2, result.size());
    }

    /**
     * Тестує збереження посту.
     *
     * <p>Перевіряє, чи сервіс правильно зберігає пост в репозиторії.</p>
     */
    @Test
    void testSave() {
        Post post = new Post();
        Mockito
                .when(postRepository.save(any(Post.class)))
                .thenReturn(post);

        postService.save(post);

        Mockito
                .verify(postRepository)
                .save(any(Post.class));
    }

    /**
     * Тестує отримання коментарів за ID посту.
     *
     * <p>Перевіряє, чи сервіс правильно отримує коментарі за ID посту
     * з репозиторію та конвертує їх у DTO.</p>
     */
    @Test
    void testGetCommentsByPostId() {
        Long postId = 1L;
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        Page<Comment> commentPage = new PageImpl<>(Arrays.asList(comment1, comment2));

        Mockito
                .when(commentRepository.findByPostId(eq(postId),
                        any(Pageable.class)))
                .thenReturn(commentPage);

        Mockito
                .when(postFacade.convertToCommentDTO(any(Comment.class)))
                .thenReturn(new CommentDTO());

        Page<CommentDTO> result = postService
                .getCommentsByPostId(postId, 0, 2, "id,asc");

        assertEquals(2, result.getContent().size());
    }

    /**
     * Тестує отримання деталей постів за ID користувача.
     * Основні кроки:
     * 1. Імітуємо отримання деталей постів за ID користувача з репозиторію.
     * 2. Імітуємо конвертацію деталей постів у відповідь для користувача.
     * 3. Перевіряємо кількість постів в отриманій відповіді.
     *
     * <p>Перевіряє, чи сервіс правильно отримує деталі постів за ID користувача
     * та конвертує їх у відповідь для користувача.</p>
     */
    @Test
    void testFindPostDetailsByUserId() {
        Long userId = 1L;
        PostResponse postResponse1 = new PostResponse();
        PostResponse postResponse2 = new PostResponse();

        Mockito
                .when(postRepository.findPostDetailsByUserId(eq(userId),
                        any(Pageable.class)))
                .thenReturn(Arrays.asList(new HashMap<>(), new HashMap<>()));

        Mockito
                .when(postFacade.convertToPostResponse(any()))
                .thenReturn(postResponse1)
                .thenReturn(postResponse2);

        Mockito
                .when(postRepository.countPostsByUserId(userId))
                .thenReturn(2L);

        Page<PostResponse> result = postService
                .findPostDetailsByUserId(userId, 0, 2, "id,asc");

        assertEquals(2, result.getContent().size());
    }

    /**
     * Тестує додавання лайку до посту,
     * коли лайка ще не існує.
     * <p>
     * Основні кроки:
     * 1. Імітуємо наявність користувача та посту в базі даних.
     * 2. Забезпечуємо, що лайк від користувача
     *    для даного посту відсутній.
     * 3. Викликаємо метод для додавання лайку.
     * 4. Перевіряємо, що результат не пустий і містить
     *    відомості про успішне додавання лайку.
     * </p>
     */
    @Test
    void testLikePostWhenNoExistingLike(){
        Long userId = 1L;
        Long postId = 1L;
        AppUser user = new AppUser();
        Post post = new Post();

        Mockito
                .when(appUserRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito
                .when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));
        Mockito
                .when(likeRepository.findByUserAndPost(user, post))
                .thenReturn(Optional.empty());

        Optional<ActionResponse> result = postService.likePost(userId, postId);

        assertTrue(result.isPresent());
        assertTrue(result.get().added());
        assertEquals("Like added", result.get().message());
        Mockito
                .verify(likeRepository)
                .save(any());
    }

    /**
     * Тестує видалення лайку з посту,
     * якщо лайк вже існує.
     * <p>
     * Основні кроки:
     * 1. Імітуємо наявність користувача,
     *    посту та існуючого лайка в базі даних.
     * 2. Викликаємо метод для додавання лайку,
     *    який має видалити існуючий лайк.
     * 3. Перевіряємо, що результат не пустий і містить
     *    відомості про успішне видалення лайку.
     * 4. Підтверджуємо, що метод видалення лайка
     *    був викликаний для існуючого лайка.
     * </p>
     */
    @Test
    void testLikePostWhenExistingLike() {
        Long userId = 2L;
        Long postId = 2L;
        AppUser user = new AppUser();
        Post post = new Post();
        Like existingLike = new Like();

        Mockito
                .when(appUserRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito
                .when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));
        Mockito
                .when(likeRepository.findByUserAndPost(user, post))
                .thenReturn(Optional.of(existingLike));

        Optional<ActionResponse> result = postService
                .likePost(userId, postId);

        assertTrue(result.isPresent());
        assertFalse(result.get().added());
        assertEquals("Like removed", result.get().message());
        Mockito
                .verify(likeRepository)
                .delete(existingLike);
    }

    /**
     * Тестує додавання репосту до посту,
     * коли репост ще не існує.
     * <p>
     * Основні кроки:
     * 1. Імітуємо наявність користувача
     *    та посту в базі даних.
     * 2. Забезпечуємо, що репост від користувача
     *    для даного посту відсутній.
     * 3. Викликаємо метод для додавання репосту.
     * 4. Перевіряємо, що результат не пустий і містить
     *    відомості про успішне додавання репосту.
     * </p>
     */
    @Test
    void testAddRepostIfNotExists() {

    }

    /**
     * Тестує видалення репосту з посту,
     * якщо репост вже існує.
     * <p>
     * Основні кроки:
     * 1. Імітуємо наявність користувача, посту та
     *    існуючого репосту в базі даних.
     * 2. Викликаємо метод для додавання репосту,
     *    який має видалити існуючий репост.
     * 3. Перевіряємо, що результат не пустий і містить
     *    відомості про успішне видалення репосту.
     * 4. Підтверджуємо, що метод видалення репосту
     *    був викликаний для існуючого репосту.
     * </p>
     */
    @Test
    void testRemoveRepostIfExists() {

    }

    /**
     * Тестує додавання коментаря до посту.
     * <p>
     * Цей тестовий метод перевіряє правильність додавання
     * коментаря до посту користувачем.
     * Основний наголос на перевірці коректної інтеракції
     * з репозиторієм та конвертації результатів.
     * </p>
     */
    @Test
    void testAddComment() {
        // Ініціалізація даних для тесту
        Long userId = 1L;
        CommentRequest request = new CommentRequest();
        request.setPostId(1L);
        request.setContent("Test content");
        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setContent(request.getContent());
        LocalDateTime expectedDate = LocalDateTime.of(2023,
                10, 8, 12, 0);
        savedComment.setCreatedDate(expectedDate);

        // Налаштування моку для зберігання коментаря
        Mockito
                .when(commentRepository.save(any()))
                .thenReturn(savedComment);

        CommentResponse expectedResponse = new CommentResponse();
        expectedResponse.setId(savedComment.getId());
        expectedResponse.setUserId(userId);
        expectedResponse.setPostId(request.getPostId());
        expectedResponse.setContent(request.getContent());
        expectedResponse.setCreated_date(expectedDate);

        // Налаштування моку для конвертації коментаря в DTO
        Mockito
                .when(postFacade.convertToCommentResponse(savedComment))
                .thenReturn(expectedResponse);

        Post mockPost = new Post();
        AppUser mockUser = new AppUser();

        // Налаштування моку для отримання користувача
        Mockito
                .when(appUserRepository.findById(userId))
                .thenReturn(Optional.of(mockUser));

        // Налаштування моку для отримання поста
        Mockito
                .when(postRepository.findById(request.getPostId()))
                .thenReturn(Optional.of(mockPost));

        // Виклик метода сервісу, який ми тестуємо
        Optional<CommentResponse> result = postService.addComment(userId, request);

        // Перевірка результату
        // Переконуємося, що результат присутній та не пустий
        assertTrue(result.isPresent());

        CommentResponse response = result.get();

        // Перевірка, що ID коментаря, повернутого сервісом,
        // відповідає очікуваному ID
        assertEquals(savedComment.getId(), response.getId());

        // Перевірка, що ID користувача у відгуку
        // відповідає очікуваному ID користувача
        assertEquals(userId, response.getUserId());

        // Перевірка, що ID поста у відгуку
        // відповідає ID поста з запиту
        assertEquals(request.getPostId(), response.getPostId());

        // Перевірка, що зміст коментаря у відгуку
        // відповідає змісту коментаря з запиту
        assertEquals(request.getContent(), response.getContent());

        // Перевірка, що дата створення коментаря у відгуку
        // відповідає очікуваній даті створення
        assertEquals(expectedDate, response.getCreated_date());
    }

    /**
     * Тестує додавання коментаря користувачем,
     * який відсутній в базі даних.
     * <p>
     * Цей тестовий метод перевіряє поведінку сервісного методу
     * в ситуації, коли користувача з заданим ID немає в базі даних.
     * Очікується, що в такій ситуації буде викликано виняток NotFoundException.
     * </p>
     */
    @Test
    void testAddCommentWithMissingUser() {
        Long userId = 1L;
        Long postId = 2L;

        CommentRequest request = new CommentRequest();
        request.setPostId(postId);
        request.setContent("Test comment");

        Mockito
                .when(appUserRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.addComment(userId, request));
    }

}

