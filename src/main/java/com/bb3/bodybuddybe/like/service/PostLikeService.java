package com.bb3.bodybuddybe.like.service;

import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface PostLikeService {
    @Transactional
    void likePost(Long postId, User user);

    @Transactional
    void unlikePost(Long postId, User user);
}
