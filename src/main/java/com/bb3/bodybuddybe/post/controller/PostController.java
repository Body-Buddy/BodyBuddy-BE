package com.bb3.bodybuddybe.post.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.post.dto.*;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import com.bb3.bodybuddybe.post.service.PostServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostServiceImpl postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponseDto> createPost(@Valid @RequestBody PostCreateRequestDto requestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.createPost(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("게시글이 작성되었습니다.", HttpStatus.CREATED.value()));

    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponseDto> getPost(@PathVariable Long postId) {
        PostDetailResponseDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/categories")
    public List<CategoryResponseDto> getCategories() {
        return postService.getCategories();
    }


    @GetMapping("/posts")
    public ResponseEntity<Page<PostSummaryResponseDto>> getPostsByCategory(@RequestParam CategoryEnum category,
                                                                           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostSummaryResponseDto> posts = postService.getPostsByCategory(category, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/gyms/{gymId}/posts")
    public ResponseEntity<Page<PostSummaryResponseDto>> getPostsByGymId(@PathVariable Long gymId,
                                                                        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostSummaryResponseDto> posts = postService.getPostsByGymId(gymId, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/search")
    public ResponseEntity<Page<PostSummaryResponseDto>> searchPosts(@RequestParam String keyword,
                                                                    @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostSummaryResponseDto> posts = postService.searchPosts(keyword, pageable);
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long postId,
                                                     @RequestBody PostUpdateRequestDto requestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(postId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("게시글이 수정되었습니다.", HttpStatus.OK.value()));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long postId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(postId, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("게시글이 삭제되었습니다.", HttpStatus.OK.value()));
    }
}