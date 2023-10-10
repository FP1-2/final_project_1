package com.facebook.controller.posts;

import com.facebook.controller.PostController;
import com.facebook.dto.post.ActionResponse;
import com.facebook.dto.post.CommentDTO;
import com.facebook.dto.post.CommentRequest;
import com.facebook.dto.post.CommentResponse;
import com.facebook.exception.ValidationErrorResponse;
import com.facebook.model.posts.Post;
import com.facebook.repository.posts.CommentRepository;
import com.facebook.repository.posts.PostRepository;
import com.facebook.service.AppUserService;
import com.facebook.service.EmailHandlerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Інтеграційний тест для класу {@link PostController}.
 * <p>
 * Цей тест забезпечує перевірку основних ендпоінтів API постів, взаємодіяючи з реальною базою даних.
 * В процесі тестування симулюється робота клієнта, який взаємодіє з API через HTTP-запити.
 * </p>
 * <p>
 * Основні сценарії тестування включають:
 * <ul>
 *     <li>{@link PostControllerTest#testGetCommentsByPostIdWithPagination() Перевірка отримання коментарів до поста з використанням пагінації}</li>
 *     <li>{@link PostControllerTest#testLikeAndUnlikePost() Перевірка логіки "лайкання" та "дизлайкання" постів}</li>
 *     <li>{@link PostControllerTest#testRepost() Перевірка логіки репостування поста (створення та видалення репоста)}</li>
 *     <li>{@link PostControllerTest#testAddComment() Перевірка додавання коментаря до публікації та обробки помилок валідації}</li>
 * </ul>
 * </p>
 * <p>
 * Додаткові компоненти, такі як {@link EmailHandlerService}, можуть бути замінені мок-версіями для ізоляції тесту.
 * </p>
 */
@Log4j2
@TestExecutionListeners(listeners = {
        //дата генератор
        GenExecutionListener.class,
        //слухач для інжекту залежності у тестовий клас
        DependencyInjectionTestExecutionListener.class,
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class PostControllerTest {

    @MockBean
    private EmailHandlerService emailHandlerService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PostRepository postRepository;

    private final String baseUrl = "http://localhost:9000/";

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders authHeaders;

    /**
     * Підготовчий метод, який викликається перед
     * кожним тестовим методом.
     * <p>
     * Цей метод здійснює аутентифікацію користувача,
     * отримує токен доступу
     * і зберігає його в заголовках, які будуть
     * використовуватися в подальших запитах до API.
     * </p>
     */
    @BeforeEach
    void setup() {
        // Об'єкт запиту для автентифікації
        AuthRequest authRequest = new AuthRequest(
                "test",
                "Password1!");

        // Запит до API для отримання токена
        ResponseEntity<AuthResponseClass> response = restTemplate
                .postForEntity(baseUrl + "api/auth/token",
                        authRequest,
                        AuthResponseClass.class);

        // Зберігаємо отриманий токен в заголовках для подальших запитів
        authHeaders = new HttpHeaders();
        String token = Objects.requireNonNull(response.getBody()).getToken();
        authHeaders.set("Authorization", "Bearer " + token);
        log.info("token: " + token);
    }

    /**
     * Тестує отримання коментарів за ID поста
     * з використанням пагінації.
     * <p>
     * Цей тест перевіряє наступне:
     * <ul>
     *     <li>Правильність відповіді API при спробі
     *     отримання коментарів до поста.</li>
     *     <li>Кількість коментарів в отриманій відповіді
     *     відповідає заданому розміру сторінки.</li>
     *     <li>Коментарі в отриманій відповіді відсортовані
     *     відповідно до заданого порядку сортування.</li>
     *     <li>Отримана кількість коментарів для даного поста
     *     відповідає очікуваній кількості коментарів в базі даних.</li>
     * </ul>
     * </p>
     */
    @Test
    void testGetCommentsByPostIdWithPagination() {
        // Знаходження поста з понад 4 коментарями
        Post targetPost = postRepository
                .findPostWithMoreThanFourComments()
                .orElse(null);

        assertNotNull(targetPost,
                "Не знайдено жодного посту з більш ніж 4 коментарями.");

        int page = 0;
        int size = 2;
        String sort = "createdDate,desc";

        // Виконання запиту до API
        ResponseEntity<PageDtoForComment<CommentDTO>> response = restTemplate
                .exchange(
                        baseUrl + "api/posts/"
                                + targetPost.getId()
                                + "/comments?page="
                                + page + "&size="
                                + size + "&sort="
                                + sort,
                        HttpMethod.GET,
                        new HttpEntity<>(authHeaders),
                        new ParameterizedTypeReference<PageDtoForComment<CommentDTO>>() {
                        });

        // Перевірка результату
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<CommentDTO> comments = response.getBody().getContent();
        assertEquals(size, comments.size());
        assertTrue(comments.get(0).getCreatedDate().isAfter(comments.get(1).getCreatedDate()));
        assertEquals(page, response.getBody().getNumber());
        assertEquals(size, response.getBody().getSize());
        Long expectedTotalComments = commentRepository.countByPostId(targetPost.getId());
        assertEquals(expectedTotalComments, response.getBody().getTotalElements());
    }

    /**
     * Тест для перевірки можливості "лайкання" та "дизлайкання" постів.
     * <p>
     * Сценарій тесту:
     * <ul>
     * <li>1. Перше "лайкання" поста.</li>
     * <li>2. Друге "лайкання" поста (має змінити стан попереднього "лайкання").</li>
     * <li>3. Спроба "лайкання" неіснуючого поста.</li>
     * </ul>
     * </p>
     */
    @Test
    void testLikeAndUnlikePost() {
        // 1. Перше "лайкання" поста
        ResponseEntity<ActionResponse> firstResponse = restTemplate.exchange(
                baseUrl + "api/posts/like/1",
                HttpMethod.POST,
                new HttpEntity<>(authHeaders),
                ActionResponse.class
        );
        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
        ActionResponse firstResponseBody = firstResponse.getBody();
        assertNotNull(firstResponseBody);

        // 2. Друге "лайкання" поста (повинно дати протилежний результат)
        ResponseEntity<ActionResponse> secondResponse = restTemplate.exchange(
                baseUrl + "api/posts/like/1",
                HttpMethod.POST,
                new HttpEntity<>(authHeaders),
                ActionResponse.class
        );
        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
        ActionResponse secondResponseBody = secondResponse.getBody();
        assertNotNull(secondResponseBody);

        assertNotEquals(firstResponseBody.added(), secondResponseBody.added());

        // 3. Сценарій з неіснуючим постом
        try {
            restTemplate.exchange(
                    //Неіснуючий пост
                    baseUrl + "api/posts/like/999",
                    HttpMethod.POST,
                    new HttpEntity<>(authHeaders),
                    ActionResponse.class
            ).getBody();
        } catch (HttpClientErrorException.NotFound e) {
            String expectedErrorMessage = """
                        {"type":"Not Found Error","message":"Post not found!"}
                    """
                    .strip();
            log.info("Реальне повідомлення про помилку: " + e.getResponseBodyAsString());
            assertEquals(expectedErrorMessage, e.getResponseBodyAsString());
            return;
        }
        // Якщо виключення не відбувається то виконання досягає рядка fail(),
        // і тест вважається невдалим
        fail("Очікувалося виключення HttpClientErrorException.NotFound");
    }

    /**
     * Тест для перевірки функціональності репостування постів.
     * <p>
     * Сценарій тесту:
     * <ul>
     * <li>1. Спроба репостити існуючий пост. Незалежно від того,
     *        чи був пост репостнутий раніше, сервіс має або створити репост,
     *        або видалити його, в залежності від поточного стану.</li>
     * <li>2. Спроба репостити той же пост знову. Сервіс має зреагувати
     *         протилежним чином, порівняно з попередньою дією (або видалити репост,
     *         якщо він був створений, або створити його знову,
     *         якщо він був видалений).</li>
     * <li>3. Порівняння результатів першого та другого репостування. Очікується,
     *        що статус репосту зміниться на протилежний.</li>
     * <li>4. Спроба репостити неіснуючий пост.
     *        Очікується отримання помилки "Post not found".</li>
     * </ul>
     * </p>
     * <p>
     * Таким чином, цей тест перевіряє поведінку функції репостування без конкретного припущення
     * про первинний стан репосту.
     * </p>
     */
    @Test
    void testRepost() {
        // 1. Сценарій з існуючим postId
        ResponseEntity<ActionResponse> firstRepostResponse = restTemplate.exchange(
                baseUrl + "api/posts/repost/1",
                HttpMethod.POST,
                new HttpEntity<>(authHeaders),
                ActionResponse.class
        );

        assertEquals(HttpStatus.OK, firstRepostResponse.getStatusCode());
        assertNotNull(firstRepostResponse.getBody());

        ResponseEntity<ActionResponse> secondRepostResponse = restTemplate.exchange(
                baseUrl + "api/posts/repost/1",
                HttpMethod.POST,
                new HttpEntity<>(authHeaders),
                ActionResponse.class
        );

        assertEquals(HttpStatus.OK, secondRepostResponse.getStatusCode());
        assertNotNull(secondRepostResponse.getBody());
        assertNotEquals(firstRepostResponse.getBody().added(), secondRepostResponse.getBody().added());

        // 2. Сценарій з неіснуючим postId

        try {
            restTemplate.exchange(
                    baseUrl + "api/posts/repost/999",
                    HttpMethod.POST,
                    new HttpEntity<>(authHeaders),
                    ActionResponse.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            String expectedErrorMessage = """
                        {"type":"Not Found Error","message":"Post not found!"}
                    """
                    .strip();
            log.info("Реальне повідомлення про помилку: " + e.getResponseBodyAsString());
            assertEquals(expectedErrorMessage, e.getResponseBodyAsString());
            return;
        }

        fail("Очікувалося виключення HttpClientErrorException.NotFound");
    }

    /**
     * Тестує додавання коментаря до публікації.
     * <p>
     * Сценарії тестування:
     * <ol>
     * <li>Відправлення коректного запиту на додавання коментаря
     *     до існуючої публікації та перевірка отриманої відповіді.</li>
     * <li>Відправлення запиту з невалідними даними коментаря
     *     та перевірка помилки валідації.</li>
     * </ol>
     * </p>
     */
    @Test
    void testAddComment() {
        // 1. Сценарій з коректним запитом
        CommentRequest validRequest = new CommentRequest();
        validRequest.setPostId(1L);
        validRequest.setContent("Це дуже цікава публікація!");

        ResponseEntity<CommentResponse> response = restTemplate.exchange(
                baseUrl + "api/posts/comment",
                HttpMethod.POST,
                new HttpEntity<>(validRequest, authHeaders),
                CommentResponse.class
        );

        // Перевірка відповіді сервера
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(validRequest.getContent(), response.getBody().getContent());

        // 2. Сценарій з невалідним запитом
        CommentRequest invalidRequest = new CommentRequest();
        invalidRequest.setPostId(null);
        invalidRequest.setContent("    "); // Запит містить лише пробіли

        try {
            restTemplate.exchange(
                    baseUrl + "api/posts/comment",
                    HttpMethod.POST,
                    new HttpEntity<>(invalidRequest, authHeaders),
                    CommentResponse.class
            );
        } catch (HttpClientErrorException.BadRequest e) {
            log.info("Отримано очікувану помилку: " + e.getResponseBodyAsString());

            ObjectMapper mapper = new ObjectMapper();
            try {
                ValidationErrorResponse actualError = mapper
                        .readValue(e.getResponseBodyAsString(),
                                ValidationErrorResponse.class);

                ValidationErrorResponse
                        .Violation expectedContentError = new ValidationErrorResponse
                        .Violation("content",
                        "Comment content cannot be empty.");
                ValidationErrorResponse
                        .Violation expectedPostIdError = new ValidationErrorResponse
                        .Violation("postId",
                        "postId cannot be null.");

                assertTrue(actualError.getViolations()
                                .contains(expectedContentError),
                        "Помилка валідації для поля 'content' не знайдена");
                assertTrue(actualError.getViolations()
                                .contains(expectedPostIdError),
                        "Помилка валідації для поля 'postId' не знайдена");

                return;
            } catch (JsonProcessingException ex) {
                fail("Помилка при обробці JSON-відповіді: " + ex.getMessage());
            }
        }
        fail("Має викидатися виключення HttpClientErrorException.BadRequest");
    }

}
