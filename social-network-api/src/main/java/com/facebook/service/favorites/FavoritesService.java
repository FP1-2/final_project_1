package com.facebook.service.favorites;

import com.facebook.dto.post.PostResponse;
import com.facebook.exception.AlreadyExistsException;
import com.facebook.exception.NotFoundException;
import com.facebook.facade.PostFacade;
import com.facebook.model.AppUser;
import com.facebook.model.favorites.Favorite;
import com.facebook.model.posts.Post;
import com.facebook.repository.favorites.FavoriteRepository;
import com.facebook.repository.posts.PostRepository;
import com.facebook.service.CurrentUserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.facebook.utils.SortUtils.getSorting;

@Log4j2
@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final PostFacade postFacade;

    private final FavoriteRepository favoriteRepository;

    private final PostRepository postRepository;

    private final CurrentUserService currentUserService;

    private final EntityManager em;

    public void addToFavorites(Long postId) {
        Long currentUserId = currentUserService.getCurrentUserId();

        favoriteRepository
                .findByUserIdAndPostId(currentUserId, postId)
                .ifPresent(fav -> {
                    throw new AlreadyExistsException("Post is already in favorites");
                });

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        Favorite favorite = new Favorite();
        favorite.setUser(em.getReference(AppUser.class, currentUserId));
        favorite.setPost(post);

        favoriteRepository.save(favorite);
    }

    public void removeFromFavorites(Long postId) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Favorite favorite = favoriteRepository.findByUserIdAndPostId(currentUserId, postId)
                .orElseThrow(() -> new NotFoundException("Favorite post not found"));

        favoriteRepository.delete(favorite);
    }

    public boolean isPostInFavorites(Long postId) {
        Long currentUserId = currentUserService.getCurrentUserId();
        return favoriteRepository.findByUserIdAndPostId(currentUserId, postId).isPresent();
    }

    public Page<PostResponse> findFavoritePostDetailsByUserId(int page,
                                                              int size,
                                                              String sort) {
        Long userId = currentUserService.getCurrentUserId();
        Sort sorting = getSorting(sort);
        Pageable pageable = PageRequest.of(page, size, sorting);

        List<Map<String, Object>> results = favoriteRepository
                .findFavoritePostDetailsByUserId(userId, pageable);

        List<PostResponse> postResponses = results
                .stream().map(postFacade::convertToPostResponse).toList();

        Long totalElements = favoriteRepository.countFavoritesByUserId(userId);

        return new PageImpl<>(postResponses, pageable, totalElements);
    }

}
