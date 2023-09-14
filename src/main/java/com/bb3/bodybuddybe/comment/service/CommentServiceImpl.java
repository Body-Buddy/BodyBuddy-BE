package com.bb3.bodybuddybe.comment.service;

import com.bb3.bodybuddybe.comment.dto.CommentRequestDto;
import com.bb3.bodybuddybe.comment.dto.CommentResponseDto;
import com.bb3.bodybuddybe.comment.dto.CommentUpdateRequestDto;
import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.comment.repository.CommentRepository;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.notification.service.NotificationService;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.repository.PostRepository;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MessageSource messageSource;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public CommentResponseDto save(UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage(
                                "not.found.post",
                                null,
                                "존재하지 않는 게시글입니다.",
                                Locale.getDefault()
                        )
                ));
        Comment comment;
        if (requestDto.getParentId() == 0) {
            comment = new Comment(requestDto.getContent(), post, userDetails.getUser());
        } else {
            //parentId > 1
            Comment parentComment = commentRepository.findById(requestDto.getParentId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
            );
            if (parentComment.getPost().getId() != requestDto.getPostId())
                throw new IllegalArgumentException("같은 게시글의 댓글이 아닙니다.");
            comment = new Comment(requestDto.getContent(), post, userDetails.getUser());

            comment.addParent(parentComment);
        }
        commentRepository.save(comment);

        notificationService.notifyToUsersThatTheyHaveReceivedComment(comment);

        return new CommentResponseDto(comment, userDetails.getUsername());
    }

    @Override
    @Transactional
    public CommentResponseDto update(Long id, UserDetailsImpl userDetails, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        if (userDetails.getUser().getId().equals(comment.getUser().getId()) || userDetails.getRole().equals(UserRoleEnum.ADMIN.toString())) {
            comment.update(requestDto);
        } else throw new IllegalArgumentException(
                messageSource.getMessage(
                        "not.authenticated",
                        null,
                        "수정/삭제 권한이 없습니다.",
                        Locale.getDefault()
                )
        );
        return new CommentResponseDto(comment, userDetails.getUsername());
    }

    @Override
    @Transactional
    public void delete(Long id, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        if (userDetails.getUser().getId().equals(comment.getUser().getId()) || userDetails.getRole().equals(UserRoleEnum.ADMIN.toString())) {
            commentRepository.delete(comment);
        } else throw new IllegalArgumentException(
                messageSource.getMessage(
                        "not.authenticated",
                        null,
                        "수정/삭제 권한이 없습니다.",
                        Locale.getDefault()
                )
        );
    }
}