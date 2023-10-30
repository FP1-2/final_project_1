package com.facebook.model.posts;

/**
 * Перелічення статусів поста в системі.
 * <p>
 * Ці статуси можуть використовуватися для відстеження життєвого циклу поста.
 * Це перелічення може бути корисним для розширення функціоналу системи.
 * </p>
 */
public enum PostStatus {
    DRAFT, PUBLISHED, ARCHIVED, REJECTED
}
