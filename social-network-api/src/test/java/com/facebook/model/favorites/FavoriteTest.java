package com.facebook.model.favorites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.facebook.model.AppUser;
import com.facebook.model.posts.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavoriteTest {

    private Favorite favorite;
    private AppUser user;
    private Post post;

    @BeforeEach
    public void setUp() {
        user = new AppUser();
        user.setId(1L);
        user.setName("Test User");

        post = new Post();
        post.setId(1L);
        post.setTitle("Test Post");

        favorite = new Favorite();
        favorite.setUser(user);
        favorite.setPost(post);
    }

    @Test
    void testFavoriteUser() {
        assertEquals(user, favorite.getUser());
    }

    @Test
    void testFavoritePost() {
        assertEquals(post, favorite.getPost());
    }

    @Test
    void testFavoriteNotNull() {
        assertNotNull(favorite);
    }

}

