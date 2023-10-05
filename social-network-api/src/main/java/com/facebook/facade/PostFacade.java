package com.facebook.facade;

import com.facebook.dto.appuser.AppUserForPost;
import com.facebook.dto.post.CommentResponse;
import com.facebook.dto.post.LikeResponse;
import com.facebook.dto.post.PostResponse;
import com.facebook.dto.post.PostSqlResult;
import com.facebook.dto.post.RepostResponse;
import com.facebook.model.posts.Comment;
import com.facebook.model.posts.Like;
import com.facebook.model.posts.Repost;
import com.facebook.repository.posts.PostRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 * Фасад для обробки та перетворення даних поста.
 * Призначений для опрацювання даних,
 * отриманих з {@link PostRepository}.
 */
@Component
@RequiredArgsConstructor
public class PostFacade {

    private final ModelMapper modelMapper;

    /**
     * Конвертує об'єкт {@link Like} у відповідний DTO {@link LikeResponse}.
     *
     * @param like об'єкт "лайка", який потрібно конвертувати
     * @return об'єкт DTO, що представляє "лайк"
     */
    public LikeResponse convertToLikeResponse(Like like) {
        LikeResponse response = modelMapper.map(like, LikeResponse.class);
        response.setCreated_date(like.getCreatedDate());
        return response;
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
     * Конвертує об'єкт {@link Repost} у відповідний DTO {@link RepostResponse}.
     *
     * @param repost об'єкт репоста, який потрібно конвертувати
     * @return об'єкт DTO, що представляє репост
     */
    public RepostResponse convertToRepostResponse(Repost repost) {
        RepostResponse response = modelMapper.map(repost, RepostResponse.class);
        response.setCreated_date(repost.getCreatedDate());
        return response;
    }

    /**
     * Перетворює результат SQL-запиту на деталі поста
     * у вигляді мапи на об'єкт PostSqlResult.
     *
     * @param row Результат SQL-запиту у вигляді мапи.
     * @return Об'єкт PostSqlResult.
     */
    public PostSqlResult mapToPostSqlResult(Map<String, Object> row) {
        return modelMapper.map(row, PostSqlResult.class);
    }

    /**
     * Перетворює результат SQL-запиту на деталі поста
     * у вигляді мапи на об'єкт відповіді PostResponse.
     * Опрацьовує деталі, такі як коментарі, лайки та репости,
     * а також інформацію про користувача.
     *
     * @param row Результат SQL-запиту у вигляді мапи.
     * @return Об'єкт PostResponse.
     */
    public PostResponse convertToPostResponse(Map<String, Object> row) {
        PostSqlResult sqlResult = mapToPostSqlResult(row);
        PostResponse response = new PostResponse();

        modelMapper.map(sqlResult, response);

        response.setComments(stringToList(sqlResult.getComment_ids()));
        response.setLikes(stringToList(sqlResult.getLike_ids()));
        response.setReposts(stringToList(sqlResult.getRepost_ids()));

        response.setUser(modelMapper.map(sqlResult, AppUserForPost.class));

        return response;
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