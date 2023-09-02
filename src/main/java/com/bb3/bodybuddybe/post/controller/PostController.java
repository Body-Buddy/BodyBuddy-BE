package com.bb3.bodybuddybe.post.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.post.dto.PostCreateRequestDto;
import com.bb3.bodybuddybe.post.dto.PostResponseDto;
import com.bb3.bodybuddybe.post.dto.PostUpdateRequestDto;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import com.bb3.bodybuddybe.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostServiceImpl postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponseDto> createPost(@RequestBody PostCreateRequestDto requestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.createPost(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("게시글이 작성되었습니다.", HttpStatus.CREATED.value()));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.getPostById(postId);

        return ResponseEntity.ok(postResponseDto);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getPostsByCategory(@RequestParam CategoryEnum category,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.getPostsByCategory(category, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/gyms/{gymId}/posts")
    public ResponseEntity<Page<PostResponseDto>> getPostsByGymId(@PathVariable Long gymId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.getPostsByGymId(gymId, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/search")
    public ResponseEntity<Page<PostResponseDto>> searchPosts(@RequestParam String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.searchPosts(keyword, pageable);
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