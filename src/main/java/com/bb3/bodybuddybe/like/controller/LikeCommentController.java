package com.bb3.bodybuddybe.like.controller;

import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.like.dto.LikeCommentResponseDto;
import com.bb3.bodybuddybe.like.service.LikeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeCommentController {
    private final LikeCommentService likeCommentService;

    @PostMapping("/like/comment/{id}")
    public ResponseEntity<LikeCommentResponseDto> like(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        return ResponseEntity.ok(likeCommentService.save(userDetails, id));
    }

    @DeleteMapping("/like/comment/{id}")
    public ResponseEntity<Long> dislike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        return ResponseEntity.ok(likeCommentService.delete(userDetails, id));
    }
}