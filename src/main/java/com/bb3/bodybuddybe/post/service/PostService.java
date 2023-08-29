package com.bb3.bodybuddybe.post.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.post.dto.*;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.entity.PostCategory;
import com.bb3.bodybuddybe.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

/**
 * 게시물 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final GymRepository gymRepository;

    /**
     * 게시물 작성
     * @param userDetails
     * @param postCreateRequestDto
     */
    public void createPost(PostCreateRequestDto postCreateRequestDto, UserDetailsImpl userDetails) {
        Gym gym = findGym(postCreateRequestDto.getGymId());

        Post post = Post.builder()
                .title(postCreateRequestDto.getTitle())
                .content(postCreateRequestDto.getContent())
                .postCategory(postCreateRequestDto.getPostCategory())
                .imageUrl(postCreateRequestDto.getImageUrl())
                .videoUrl(postCreateRequestDto.getVideoUrl())
                .user(userDetails.getUser())
                .gym(gym)
                .build();

        postRepository.save(post);
    }

    /**
     * 게시물 조회
     * @param postId
     * @return PostResponseDto
     * @throws RejectedExecutionException 게시물이 없을 경우
     */
    public PostResponseDto getPostById(Long postId) {
        Post post = findPost(postId);

        return new PostResponseDto(post);
    }

    /**
     * 헬스장 ID로 게시물 조회
     * @param gymId
     * @return PostListResponseDto
     * @throws RejectedExecutionException 게시물이 없을 경우
     */
    public PostListResponseDto getPostsByGymId(Long gymId) {
        List<PostResponseDto> postList = postRepository.findAllByGymId(gymId).stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }

    /**
     * 카테고리로 게시물 조회
     * @param postCategory
     * @return PostCategoryListResponseDto
     * @throws RejectedExecutionException 게시물이 없을 경우
     */
    public PostCategoryListResponseDto getPostsByCategory(PostCategory postCategory) {
        PostCategoryListResponseDto postCategoryListResponseDto = new PostCategoryListResponseDto(postRepository.findByPostCategory(postCategory)
                .stream()
                .map(PostResponseDto::new)
                .toList());

        return postCategoryListResponseDto;
    }

    /**
     * 제목으로 게시물 조회
     * @param title
     * @return PostTitleListResponseDto
     * @throws RejectedExecutionException 게시물이 없을 경우
     */
    public PostTitleListResponseDto getPostsByTitle(String title) {
        PostTitleListResponseDto postTitleListResponseDto = new PostTitleListResponseDto(postRepository.findByTitle(title)
                .stream()
                .map(PostResponseDto::new)
                .toList());

        if (postTitleListResponseDto.getPostTitleList().isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_POST);
        }

        return postTitleListResponseDto;
    }

    /**
     * 게시물 수정
     * @param postId
     * @param postCreateRequestDto
     * @param userDetails
     * @throws RejectedExecutionException 작성자가 다른 경우
     */
    @Transactional
    public void updatePost(Long postId, PostCreateRequestDto postCreateRequestDto, UserDetailsImpl userDetails) {
        Post post = findPost(postId);

        if (!post.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorCode.NOT_POST_WRITER);
        }

        post.update(postCreateRequestDto.getTitle(),
                postCreateRequestDto.getContent(),
                postCreateRequestDto.getPostCategory(),
                postCreateRequestDto.getImageUrl(),
                postCreateRequestDto.getVideoUrl());

        postRepository.save(post);
    }

    /**
     * 게시물 삭제
     * @param postId
     * @param userDetails
     * @throws RejectedExecutionException 게시물이 없을 경우
     */
    public void deletePost(Long postId, UserDetailsImpl userDetails) {
        Post post = findPost(postId);

        if (!post.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorCode.NOT_POST_WRITER);
        }

        postRepository.delete(post);
    }

    /**
     * 게시물 ID로 게시물 찾기
     * @param id
     * @return Post
     * @throws RejectedExecutionException 게시물이 없을 경우
     */
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );
    }

    /**
     * 헬스장 ID로 헬스장 찾기
     * @param id
     * @return Gym
     * @throws RejectedExecutionException 헬스장이 없을 경우
     */
    public Gym findGym(Long id) {
        return gymRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_GYM)
        );
    }
}
