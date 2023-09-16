package com.bb3.bodybuddybe.comment.service;

import com.bb3.bodybuddybe.comment.dto.CommentCreateRequestDto;
import com.bb3.bodybuddybe.comment.dto.CommentUpdateRequestDto;
import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.comment.repository.CommentRepository;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.notification.service.NotificationService;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.repository.PostRepository;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void createComment(CommentCreateRequestDto requestDto, User user) {
        Post post = findPost(requestDto.getPostId());

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .post(post)
                .author(user)
                .build();

        if (requestDto.getParentId() != 0) {
            Comment parent = validateParentComment(requestDto);
            if (parent.getParent() == null) {
                comment.addParent(parent);
            } else {
                throw new CustomException(ErrorCode.NOT_SUPPORTED_COMMENT_DEPTH);
            }
        }

        commentRepository.save(comment);
        notificationService.notifyToUsersThatTheyHaveReceivedComment(comment);
    }

    private Comment validateParentComment(CommentCreateRequestDto requestDto) {
        Comment parent = findComment(requestDto.getParentId());
        if (!parent.getPost().getId().equals(requestDto.getPostId())) {
            throw new CustomException(ErrorCode.WRONG_PARENT_COMMENT);
        }
        return parent;
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, CommentUpdateRequestDto requestDto, User user) {
        Comment comment = findComment(commentId);
        validateUserOwnership(comment, user);
        comment.update(requestDto);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        validateUserOwnership(comment, user);
        commentRepository.delete(comment);
    }

    private void validateUserOwnership(Comment comment, User user) {
        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new CustomException(ErrorCode.NOT_COMMENT_AUTHOR);
        }
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
