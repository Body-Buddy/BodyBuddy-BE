package com.bb3.bodybuddybe.comment.service;

import com.bb3.bodybuddybe.comment.dto.CommentCreateRequestDto;
import com.bb3.bodybuddybe.comment.dto.CommentUpdateRequestDto;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {
    @Transactional
    void createComment(CommentCreateRequestDto requestDto, User user);

    @Transactional
    void updateComment(Long commentId, CommentUpdateRequestDto requestDto, User user);

    @Transactional
    void deleteComment(Long commentId, User user);
}
