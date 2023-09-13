package com.bb3.bodybuddybe.like.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.like.entity.PostLike;
import com.bb3.bodybuddybe.like.repository.PostLikeRepository;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.repository.PostRepository;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    @Transactional
    public void likePost(Long postId, User user) {
        Post post = findPost(postId);
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED_POST);
        }
        postLikeRepository.save(new PostLike(user, post));
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, User user) {
        Post post = findPost(postId);
        PostLike postLike = postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_LIKE_NOT_FOUND));
        postLikeRepository.delete(postLike);
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }
}
