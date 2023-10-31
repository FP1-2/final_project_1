package com.facebook.controller;

import com.facebook.dto.post.PostResponse;
import com.facebook.service.CurrentUserService;
import com.facebook.service.favorites.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контролер для роботи з улюбленими постами користувача.
 * <ul>
 *     <li>{@link #addToFavorites(Long)} додає пост до вибраного користувача.</li>
 *     <li>{@link #removeFromFavorites(Long)} видаляє пост з вибраного користувача.</li>
 *     <li>{@link #isPostInFavorites(Long)} перевіряє, чи є пост у вибраному користувача.</li>
 *     <li>{@link #getFavoritePosts(int, int, String)} повертає список улюблених постів користувача з пагінацією.</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final CurrentUserService currentUserService;

    private final FavoritesService favoritesService;

    @PostMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addToFavorites(@PathVariable Long postId) {
        favoritesService.addToFavorites(postId,
                currentUserService.getCurrentUserId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long postId) {
        favoritesService.removeFromFavorites(postId,
                currentUserService.getCurrentUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{postId}/exists")
    public ResponseEntity<Boolean> isPostInFavorites(@PathVariable Long postId) {
        boolean exists = favoritesService
                .isPostInFavorites(postId, currentUserService.getCurrentUserId());
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getFavoritePosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        Page<PostResponse> favoritePosts = favoritesService
                .findFavoritePostDetailsByUserId(currentUserService
                        .getCurrentUserId(),page, size, sort);
        return new ResponseEntity<>(favoritePosts, HttpStatus.OK);
    }

}

