package com.bb3.bodybuddybe.like.service;

import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface CommentLikeService {
    @Transactional
    void likeComment(Long commentId, User user);

    @Transactional
    void unlikeComment(Long commentId, User user);
}
