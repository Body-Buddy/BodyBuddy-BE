package com.bb3.bodybuddybe.post.controller;

import com.amazonaws.Response;
import com.bb3.bodybuddybe.post.dto.PostCreateRequestDto;
import com.bb3.bodybuddybe.post.dto.PostResponseDto;
import com.bb3.bodybuddybe.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    public ResponseEntity<PostResponseDto>createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostCreateRequestDto postCreateRequestDto){
        PostResponseDto postResponseDto = postService.createPost(userDetails.getUser(), postCreateRequestDto);

        return ResponseEntity.status(201).body(postResponseDto);

    }
}
