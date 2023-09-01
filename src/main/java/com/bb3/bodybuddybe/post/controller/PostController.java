package com.bb3.bodybuddybe.post.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.post.dto.*;
import com.bb3.bodybuddybe.post.entity.PostCategory;
import com.bb3.bodybuddybe.post.service.PostService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 게시물 관련 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    /**
     * 게시물 작성
     * @param userDetails
//     * @param postCreateRequestDto
     * @return ApiResponseDto
     */
    @PostMapping("/posts")
    public ResponseEntity<ApiResponseDto> createPost(@RequestPart(value = "title") String title,
                                                     @RequestPart(value = "content") String content,
                                                     @Nullable @RequestPart(value = "file") List<MultipartFile> files,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostCreateRequestDto requestDto = new PostCreateRequestDto(title, content);
        postService.createPost(requestDto, userDetails, files);
        return ResponseEntity.ok().body(new ApiResponseDto("게시글이 작성되었습니다.", HttpStatus.CREATED.value()));
    }

    /**
     * 게시물 조회
     * @param postId
     * @return PostResponseDto
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.getPostById(postId);

        return ResponseEntity.ok().body(postResponseDto);
    }

    /**
     * 헬스장 게시물 목록 조회
     * @param gymId
     * @return PostListResponseDto
     */
    @GetMapping("/gyms/{gymId}/posts")
    public ResponseEntity<PostListResponseDto> getPostsByGymId(@PathVariable Long gymId) {
        PostListResponseDto postListResponseDto = postService.getPostsByGymId(gymId);

        return ResponseEntity.ok().body(postListResponseDto);
    }

    /**
     * 게시물 카테고리 조회
     * @param postCategory
     * @return PostListResponseDto
     */
    @GetMapping("/posts/category")
    public ResponseEntity<PostCategoryListResponseDto> getPostsByCategory(@RequestParam PostCategory postCategory) {
        PostCategoryListResponseDto postCategoryListResponseDto = postService.getPostsByCategory(postCategory);

        return ResponseEntity.ok().body(postCategoryListResponseDto);
    }
    /**
     * 게시물 제목으로 조회
     * @param title
     * @return PostTitleListResponseDto
     */
    @GetMapping("/posts/title")
    public ResponseEntity<PostTitleListResponseDto> getPostsByTitle(@RequestParam String title) {
        PostTitleListResponseDto postTitleListResponseDto = postService.getPostsByTitle(title);

        return ResponseEntity.ok().body(postTitleListResponseDto);
    }

    /**
     * 게시물 수정
     * @param postId
     * @param postCreateRequestDto
     * @param userDetails
     * @return ApiResponseDto
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long postId,
                                                     @RequestBody PostCreateRequestDto postCreateRequestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(postId, postCreateRequestDto, userDetails);

        return ResponseEntity.ok().body(new ApiResponseDto("게시글이 수정되었습니다.", HttpStatus.OK.value()));
    }

    /**
     * 게시물 삭제
     * @param userDetails
     * @param postId
     * @return ApiResponseDto
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable Long postId) {
        postService.deletePost(postId, userDetails);

        return ResponseEntity.ok().body(new ApiResponseDto("게시글이 삭제되었습니다.", HttpStatus.OK.value()));
    }
}