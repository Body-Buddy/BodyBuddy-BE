package com.bb3.bodybuddybe.post.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.post.dto.*;
import com.bb3.bodybuddybe.post.entity.PostCategory;
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

    //게시물 생성
    @PostMapping("/posts")
    public ResponseEntity<ApiResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @RequestBody PostCreateRequestDto postCreateRequestDto) {
        postService.createPost(postCreateRequestDto, userDetails);
        return ResponseEntity.ok().body(new ApiResponseDto("게시글이 작성되었습니다.", HttpStatus.CREATED.value()));
    }

    //게시물 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.getPostById(postId);

        return ResponseEntity.ok().body(postResponseDto);
    }

    //헬스장 ID로 게시물 조회
    @GetMapping("/gyms/{gymId}/posts")
    public ResponseEntity<PostListResponseDto> getPostsByGymId(@PathVariable Long gymId) {
        PostListResponseDto postListResponseDto = postService.getPostsByGymId(gymId);

        return ResponseEntity.ok().body(postListResponseDto);
    }

    //카테고리로 게시물 조회
    @GetMapping("/posts/category")
    public ResponseEntity<PostCategoryListResponseDto> getPostsByCategory(@RequestParam PostCategory postCategory) {
        PostCategoryListResponseDto postCategoryListResponseDto = postService.getPostsByCategory(postCategory);

        return ResponseEntity.ok().body(postCategoryListResponseDto);
    }
    //제목으로 게시물 조회
    @GetMapping("/posts/title")
    public ResponseEntity<PostTitleListResponseDto> getPostsByTitle(@RequestParam String title) {
        PostTitleListResponseDto postTitleListResponseDto = postService.getPostsByTitle(title);

        return ResponseEntity.ok().body(postTitleListResponseDto);
    }

    //게시물 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long postId,
                                                     @RequestBody PostCreateRequestDto postCreateRequestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(postId, postCreateRequestDto, userDetails);

        return ResponseEntity.ok().body(new ApiResponseDto("게시글이 수정되었습니다.", HttpStatus.OK.value()));
    }

    //게시물 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable Long postId) {
        postService.deletePost(postId, userDetails);

        return ResponseEntity.ok().body(new ApiResponseDto("게시글이 삭제되었습니다.", HttpStatus.OK.value()));
    }
}