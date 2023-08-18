package com.bb3.bodybuddybe.post.controller;

import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.post.dto.PostCreateRequestDto;
import com.bb3.bodybuddybe.post.dto.PostListResponseDto;
import com.bb3.bodybuddybe.post.dto.PostResponseDto;
import com.bb3.bodybuddybe.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostCreateRequestDto postCreateRequestDto) {
        PostResponseDto postResponseDto = postService.createPost(postCreateRequestDto, userDetails.getUser());

        return ResponseEntity.status(201).body(postResponseDto);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.getPostById(postId);

        return ResponseEntity.ok().body(postResponseDto);
    }

//    @GetMapping("/gyms/{gymId}/posts")
//    public ResponseEntity<PostListResponseDto> getPostsByGymId(@PathVariable Long gymId) {
//        PostListResponseDto postListResponseDto = postService.getPostsByGymId(gymId);
//
//        return ResponseEntity.ok().body(postListResponseDto);
//    }
//
}
