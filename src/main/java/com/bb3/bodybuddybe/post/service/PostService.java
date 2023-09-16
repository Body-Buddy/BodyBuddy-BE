package com.bb3.bodybuddybe.post.service;

import com.bb3.bodybuddybe.post.dto.*;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface PostService {

    @Transactional
    void createPost(PostCreateRequestDto requestDto, List<MultipartFile> files, User user);

    @Transactional(readOnly = true)
    PostDetailResponseDto getPostById(Long postId);

    List<CategoryResponseDto> getCategories();

    @Transactional(readOnly = true)
    Page<PostSummaryResponseDto> getPostsByCategory(CategoryEnum category, Pageable pageable);

    @Transactional(readOnly = true)
    Page<PostSummaryResponseDto> getPostsByGymId(Long gymId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<PostSummaryResponseDto> searchPosts(String keyword, Pageable pageable);

    @Transactional
    void updatePost(Long postId, PostUpdateRequestDto requestDto, User user);

    @Transactional
    void deletePost(Long postId, User user);
}
