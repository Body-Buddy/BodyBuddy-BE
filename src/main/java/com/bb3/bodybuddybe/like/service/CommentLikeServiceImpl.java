package com.bb3.bodybuddybe.like.service;

import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.comment.repository.CommentRepository;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.like.entity.CommentLike;
import com.bb3.bodybuddybe.like.repository.CommentLikeRepository;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    @Transactional
    public void likeComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        if (commentLikeRepository.existsByUserAndComment(user, comment)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED_COMMENT);
        }
        commentLikeRepository.save(new CommentLike(user, comment));
    }

    @Override
    @Transactional
    public void unlikeComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        CommentLike commentLike = commentLikeRepository.findByUserAndComment(user, comment)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_LIKE_NOT_FOUND));
        commentLikeRepository.delete(commentLike);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
