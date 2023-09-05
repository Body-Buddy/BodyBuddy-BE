package com.bb3.bodybuddybe.post.service;

import com.bb3.bodybuddybe.post.dto.CategoryResponseDto;
import com.bb3.bodybuddybe.post.dto.PostCreateRequestDto;
import com.bb3.bodybuddybe.post.dto.PostResponseDto;
import com.bb3.bodybuddybe.post.dto.PostUpdateRequestDto;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface PostService {

    @Transactional
    void createPost(PostCreateRequestDto requestDto, User user, List<MultipartFile> files);

    @Transactional(readOnly = true)
    PostResponseDto getPostById(Long postId);

    List<CategoryResponseDto> getCategories();

    @Transactional(readOnly = true)
    Page<PostResponseDto> getPostsByCategory(CategoryEnum category, Pageable pageable);

    @Transactional(readOnly = true)
    Page<PostResponseDto> getPostsByGymId(Long gymId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<PostResponseDto> searchPosts(String keyword, Pageable pageable);

    @Transactional
    void updatePost(Long postId, PostUpdateRequestDto requestDto, User user);

    @Transactional
    void deletePost(Long postId, User user);
}
