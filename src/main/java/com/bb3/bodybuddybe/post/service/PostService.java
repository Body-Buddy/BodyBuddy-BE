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

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final GymRepository gymRepository;

    //게시물 생성
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

    //게시물 조회
    public PostResponseDto getPostById(Long postId) {
        Post post = findPost(postId);

        return new PostResponseDto(post);
    }

    //헬스장 ID로 게시물 조회
    public PostListResponseDto getPostsByGymId(Long gymId) {
        List<PostResponseDto> postList = postRepository.findAllByGymId(gymId).stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }

    //카테고리로 게시물 조회
    public PostCategoryListResponseDto getPostsByCategory(PostCategory postCategory) {
        PostCategoryListResponseDto postCategoryListResponseDto = new PostCategoryListResponseDto(postRepository.findByPostCategory(postCategory)
                .stream()
                .map(PostResponseDto::new)
                .toList());

        return postCategoryListResponseDto;
    }

    //제목으로 게시물 조회
    public PostTitleListResponseDto getPostsByTitle(String postTitle) {
        PostTitleListResponseDto postTitleListResponseDto = new PostTitleListResponseDto(postRepository.findByPostTitle(postTitle)
                .stream()
                .map(PostResponseDto::new)
                .toList());

        return postTitleListResponseDto;
    }

    //게시물 수정
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

    //게시물 삭제
    public void deletePost(Long postId, UserDetailsImpl userDetails) {
        Post post = findPost(postId);

        if (!post.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorCode.NOT_POST_WRITER);
        }

        postRepository.delete(post);
    }

    //게시물 ID로 게시물 찾기
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );
    }

    //헬스장 ID로 헬스장 찾기
    public Gym findGym(Long id) {
        return gymRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );
    }
}
