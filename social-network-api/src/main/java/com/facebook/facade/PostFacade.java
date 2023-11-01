package com.facebook.facade;

import com.facebook.dto.post.Author;
import com.facebook.dto.post.CommentDTO;
import com.facebook.dto.post.CommentResponse;
import com.facebook.dto.post.PostRequest;
import com.facebook.dto.post.PostResponse;
import com.facebook.dto.post.PostSqlResult;
import com.facebook.dto.post.RepostRequest;
import com.facebook.model.AppUser;
import com.facebook.model.posts.Comment;
import com.facebook.model.posts.Post;
import com.facebook.model.posts.PostStatus;
import com.facebook.repository.favorites.FavoriteRepository;
import com.facebook.repository.posts.PostRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Фасад для обробки та перетворення даних поста.
 * Призначений для опрацювання даних,
 * отриманих з {@link PostRepository}, {@link FavoriteRepository}.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class PostFacade {

    private final ModelMapper modelMapper;

    /**
     * Конвертує об'єкт {@link Comment} у відповідний DTO {@link CommentDTO}.
     *
     * @param comment об'єкт коментаря, який потрібно конвертувати
     * @return об'єкт DTO, що представляє коментар
     */
    public CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        Author appUserForPost = modelMapper.map(comment.getUser(), Author.class);
        commentDTO.setAppUser(appUserForPost);
        return commentDTO;
    }

    /**
     * Конвертує {@link PostRequest} в об'єкт {@link Post}
     * використовуючи заданого користувача.
     *
     * @param request об'єкт типу {@link PostRequest},
     *                що містить дані для створення поста.
     * @param user    об'єкт типу {@link AppUser},
     *                який стане власником поста.
     * @return об'єкт типу {@link Post}, створений на основі
     *                {@link PostRequest} та {@link AppUser}.
     */
    public Post convertPostRequestToPost(PostRequest request, AppUser user) {
        Post post = new Post();
        post.setImageUrl(request.getImageUrl());
        post.setTitle(request.getTitle());
        post.setBody(request.getBody());
        post.setStatus(PostStatus.PUBLISHED);
        post.setType(request.getType());
        post.setUser(user);
        return post;
    }

    public Post convertRepostRequestToPost(RepostRequest request, AppUser user){
        Post post = new Post();
        post.setImageUrl(request.getImageUrl());
        post.setTitle(request.getTitle());
        post.setBody(request.getBody());
        post.setStatus(PostStatus.PUBLISHED);
        post.setType(request.getType());
        post.setUser(user);
        post.setOriginalPostId(request.getOriginalPostId());
        return post;
    }

    /**
     * Конвертує об'єкт {@link Comment} у відповідний DTO {@link CommentResponse}.
     *
     * @param comment об'єкт коментаря, який потрібно конвертувати
     * @return об'єкт DTO, що представляє коментар
     */
    public CommentResponse convertToCommentResponse(Comment comment) {
        CommentResponse response = modelMapper.map(comment, CommentResponse.class);
        response.setCreated_date(comment.getCreatedDate());
        return response;
    }

    /**
     * Перетворює результат SQL-запиту на деталі поста з мапи в об'єкт PostSqlResult.
     * Оскільки dev використовує базу даних h2, де ключі в верхньому регістрі,
     * а prod - MySQL в нижньому,
     * цей метод перетворює всі ключі в верхній регістр для уніфікації перед маппінгом.
     * Це забезпечує коректну роботу мапера незалежно від особливостей реалізації бази даних.
     *
     * @param row Результат SQL-запиту у вигляді мапи.
     * @return Об'єкт PostSqlResult.
     */
    public PostSqlResult mapToPostSqlResult(Map<String, Object> row) {
        Map<String, Object> resultMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : row.entrySet()) {
            resultMap.put(entry.getKey().toUpperCase(), entry.getValue());
        }

        PostSqlResult result = modelMapper.map(resultMap, PostSqlResult.class);

        result.setCommentIds((String) resultMap.get("COMMENT_IDS"));
        result.setLikeIds((String) resultMap.get("LIKE_IDS"));
        result.setRepostIds((String) resultMap.get("REPOST_IDS"));

        result.setOriginalCommentIds((String) resultMap.get("ORIGINAL_COMMENT_IDS"));
        result.setOriginalLikeIds((String) resultMap.get("ORIGINAL_LIKE_IDS"));
        result.setOriginalRepostIds((String) resultMap.get("ORIGINAL_REPOST_IDS"));

        return result;

    }

    /**
     * Перетворює результат SQL-запиту про деталі поста на об'єкт
     * відповіді {@link PostResponse}.
     * <p>Метод використовує допоміжний метод для опрацювання результатів, асоційованих з репостами.
     * Інформація про автора, коментарі, лайки та інші деталі додаються до відповіді.
     * Якщо реалізувати репости репостів додати:
     * <p>{@code post.setReposts(stringToList(sqlResult.getCurrentReposts()));}</p></p>
     *
     * @param row Результат SQL-запиту у вигляді мапи.
     * @return Перетворений об'єкт {@link PostResponse}.
     */
    public PostResponse convertToPostResponse(Map<String, Object> row) {
        PostSqlResult sqlResult = mapToPostSqlResult(row);

        PostResponse post = new PostResponse();

        post.setAuthor(modelMapper.map(sqlResult, Author.class));
        modelMapper.map(sqlResult, post);

        Optional.ofNullable(sqlResult.getCommentIds())
                .map(this::stringToList)
                .ifPresent(post::setComments);

        Optional.ofNullable(sqlResult.getLikeIds())
                .map(this::stringToList)
                .ifPresent(post::setLikes);

        Optional.ofNullable(sqlResult.getRepostIds())
                .map(this::stringToList)
                .ifPresent(post::setReposts);

        Optional.ofNullable(sqlResult.getOriginalPostId())
                .map(id -> mapOriginalPost(sqlResult))
                .ifPresent(post::setOriginalPost);

        return post;
    }

    /**
     * Перетворює результат SQL-запиту, асоційований з оригінальним постом,
     * на об'єкт відповіді {@link PostResponse}.
     * <p>Метод створює відповідь для репостів, де оригінальний пост додається як частина результату.
     * Він заповнює інформацію про автора оригінального поста та додаткові деталі,
     * такі як коментарі, лайки та репости.</p>
     *
     * @param sqlResult Результат SQL-запиту, що представляє деталі оригінального посту.
     * @return Перетворений об'єкт {@link PostResponse} для оригінального посту.
     */
    private PostResponse mapOriginalPost(PostSqlResult sqlResult){

        PostResponse originalPost = new PostResponse();

        modelMapper.map(sqlResult, originalPost);

        Author originalAuthor = new Author();
        originalAuthor.setUserId(sqlResult.getOriginalUserId());
        originalAuthor.setName(sqlResult.getOriginalName());
        originalAuthor.setSurname(sqlResult.getOriginalSurname());
        originalAuthor.setUsername(sqlResult.getOriginalUsername());
        originalAuthor.setAvatar(sqlResult.getOriginalAvatar());

        originalPost.setAuthor(originalAuthor);

        originalPost.setPostId(sqlResult.getOriginalPostId());
        originalPost.setTitle(sqlResult.getOriginalTitle());
        originalPost.setBody(sqlResult.getOriginalBody());
        originalPost.setStatus(sqlResult.getOriginalStatus());
        originalPost.setType(sqlResult.getOriginalType());

        Optional.ofNullable(sqlResult.getOriginalCommentIds())
                .map(this::stringToList)
                .ifPresent(originalPost::setComments);

        Optional.ofNullable(sqlResult.getOriginalLikeIds())
                .map(this::stringToList)
                .ifPresent(originalPost::setLikes);

        Optional.ofNullable(sqlResult.getOriginalRepostIds())
                .map(this::stringToList)
                .ifPresent(originalPost::setReposts);

        return originalPost;
    }

    /**
     * Перетворює рядкове представлення списку ID (розділених комами) у список Long.
     * <p>
     * Використовується для перетворення рядків,
     * що містять список ID "comments", "likes", "reposts",
     * отриманих з бази даних, у список об'єктів Long для подальшого використання у DTO.
     * </p>
     *
     * @param source Рядкове представлення списку ID.
     * @return Список ID як об'єкти Long.
     */
    private List<Long> stringToList(String source) {
        return Optional.ofNullable(source)
                .filter(input -> !input.isEmpty())
                .map(input -> Arrays.stream(input.split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

}