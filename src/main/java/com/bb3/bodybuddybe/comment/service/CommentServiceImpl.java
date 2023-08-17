package com.bb3.bodybuddybe.comment.service;

import com.bb3.bodybuddybe.comment.dto.CommentRequestDto;
import com.bb3.bodybuddybe.comment.dto.CommentResponseDto;
import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CardServiceImpl cardService;

    @Override
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Card card = cardService.findCard(requestDto.getCardId());
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setCard(card);
        comment.setDescription(requestDto.getDescription());
        comment.setUsername(requestDto.getUsername());

        var savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);
    }

    @Override
    public void deleteComment(Comment comment, User user) {
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(Comment comment, CommentRequestDto requestDto, User user) {

        comment.setUsername(requestDto.getUsername());
        comment.setDescription(requestDto.getDescription());

        return new CommentResponseDto(comment);
    }

    @Override
    public Comment findComment(long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 card는 존재하지 않습니다.")
        );
    }
}

