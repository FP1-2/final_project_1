package com.facebook.service;

import com.facebook.model.AppUser;
import com.facebook.model.posts.Comment;
import com.facebook.model.posts.Like;
import com.facebook.model.posts.Post;
import com.facebook.model.posts.Repost;
import com.facebook.repository.posts.CommentRepository;
import com.facebook.repository.posts.LikeRepository;
import com.facebook.repository.posts.PostRepository;
import com.facebook.repository.posts.RepostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;

    private final RepostRepository repostRepository;

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    // Logic for liking a post
    public void likePost(AppUser user, Post post) {
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);
    }

    // Logic for commenting on a post
    public void addComment(AppUser user, Post post, String commentText) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(commentText);
        commentRepository.save(comment);
    }

    // Logic for reposting a post
    public void repost(AppUser user, Post originalPost) {
        Repost repost = new Repost();
        repost.setUser(user);
        repost.setPost(originalPost);
        repostRepository.save(repost);
    }

    public Page<Post> findByUserId(Long userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable);
    }

}
