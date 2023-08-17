package com.bb3.bodybuddybe.comment.service;

import com.bb3.bodybuddybe.comment.dto.CommentRequestDto;
import com.bb3.bodybuddybe.comment.dto.CommentResponseDto;
import com.bb3.bodybuddybe.comment.entity.Comment;
import com.passion.teampassiontrelloproject.comment.dto.CommentRequestDto;
import com.passion.teampassiontrelloproject.comment.dto.CommentResponseDto;
import com.passion.teampassiontrelloproject.comment.entity.Comment;
import com.passion.teampassiontrelloproject.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {


    CommentResponseDto createComment(CommentRequestDto requestDto, User user);

    void deleteComment(Comment comment, User user);

    Comment findComment(long id);

    @Transactional
    CommentResponseDto updateComment(Comment comment, CommentRequestDto requestDto, User user);

}
