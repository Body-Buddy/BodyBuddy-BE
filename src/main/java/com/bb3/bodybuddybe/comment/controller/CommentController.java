package com.bb3.bodybuddybe.comment.controller;

import com.bb3.bodybuddybe.comment.dto.CommentCreateRequestDto;
import com.bb3.bodybuddybe.comment.dto.CommentUpdateRequestDto;
import com.bb3.bodybuddybe.comment.service.CommentServiceImpl;
import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/comments")
    public ResponseEntity<ApiResponseDto> createComment(@Valid @RequestBody CommentCreateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("댓글 생성 성공", HttpStatus.CREATED.value()));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(commentId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("댓글 수정 성공", HttpStatus.OK.value()));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponseDto> delete(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("댓글 삭제 성공", HttpStatus.OK.value()));
    }
}