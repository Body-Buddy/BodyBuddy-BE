package com.bb3.bodybuddybe.comment.service;

import com.bb3.bodybuddybe.comment.dto.CommentRequestDto;
import com.bb3.bodybuddybe.comment.dto.CommentResponseDto;
import com.bb3.bodybuddybe.comment.dto.CommentUpdateRequestDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {


    @Transactional
    CommentResponseDto save(UserDetailsImpl userDetails, CommentRequestDto requestDto);

    @Transactional
    CommentResponseDto update(Long id, UserDetailsImpl userDetails, CommentUpdateRequestDto requestDto);

    @Transactional
    void delete(Long id, UserDetailsImpl userDetails);


    }