package com.bb3.bodybuddybe.like.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.like.service.PostLikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostLikeController {

    private final PostLikeServiceImpl postLikeService;

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<ApiResponseDto> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postLikeService.likePost(postId, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("게시글 좋아요 성공", HttpStatus.OK.value()));
    }

    @DeleteMapping("/posts/{postId}/likes")
    public ResponseEntity<ApiResponseDto> unlikePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postLikeService.unlikePost(postId, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("게시글 좋아요 취소 성공", HttpStatus.OK.value()));
    }
}