package com.bb3.bodybuddybe.post.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.post.dto.PostCreateRequestDto;
import com.bb3.bodybuddybe.post.dto.PostResponseDto;
import com.bb3.bodybuddybe.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @RequestBody PostCreateRequestDto postCreateRequestDto) {
        postService.createPost(postCreateRequestDto, userDetails);
        return ResponseEntity.ok().body(new ApiResponseDto("게시글이 작성되었습니다.", HttpStatus.CREATED.value()));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.getPostById(postId);

        return ResponseEntity.ok().body(postResponseDto);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long postId,
                                                     @RequestBody PostCreateRequestDto postCreateRequestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(postId, postCreateRequestDto, userDetails);

        return ResponseEntity.ok().body(new ApiResponseDto("게시글이 수정되었습니다.", HttpStatus.OK.value()));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable Long postId) {
        postService.deletePost(postId, userDetails);

        return ResponseEntity.ok().body(new ApiResponseDto("게시글이 삭제되었습니다.", HttpStatus.OK.value()));
    }
}

//    @GetMapping("/gyms/{gymId}/posts")
//    public ResponseEntity<PostListResponseDto> getPostsByGymId(@PathVariable Long gymId) {
//        PostListResponseDto postListResponseDto = postService.getPostsByGymId(gymId);
//
//        return ResponseEntity.ok().body(postListResponseDto);
//    }
////
