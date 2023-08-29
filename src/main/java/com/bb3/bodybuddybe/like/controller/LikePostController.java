package com.bb3.bodybuddybe.like.controller;

import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.like.dto.LikePostResponseDto;
import com.bb3.bodybuddybe.like.service.LikePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikePostController {

    private final LikePostService likePostService;

    @PostMapping("/like/posts/{id}")
    public ResponseEntity<LikePostResponseDto> like(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        LikePostResponseDto responseDto = likePostService.save(userDetails, id);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/like/posts/{id}")
    public ResponseEntity<Long> dislike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id){
        return ResponseEntity.ok(likePostService.delete(userDetails, id));
    }
}