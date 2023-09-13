package com.bb3.bodybuddybe.like.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.like.service.CommentLikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentLikeController {

    private final CommentLikeServiceImpl commentLikeService;

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<ApiResponseDto> likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentLikeService.likeComment(commentId, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("댓글 좋아요 성공", HttpStatus.OK.value()));
    }

    @DeleteMapping("/comments/{commentId}/likes")
    public ResponseEntity<ApiResponseDto> unlikeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentLikeService.unlikeComment(commentId, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("댓글 좋아요 취소 성공", HttpStatus.OK.value()));
    }
}
