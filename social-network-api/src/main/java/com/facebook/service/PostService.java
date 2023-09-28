package com.facebook.service;

import com.facebook.repository.posts.CommentRepository;
import com.facebook.repository.posts.LikeRepository;
import com.facebook.repository.posts.PostRepository;
import com.facebook.repository.posts.RepostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private PostRepository postRepository;

    private LikeRepository likeRepository;

    private CommentRepository commentRepository;

    private RepostRepository repostRepository;
}
