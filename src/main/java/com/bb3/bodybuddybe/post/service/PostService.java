package com.bb3.bodybuddybe.post.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.post.dto.PostCreateRequestDto;
import com.bb3.bodybuddybe.post.dto.PostListResponseDto;
import com.bb3.bodybuddybe.post.dto.PostResponseDto;
import com.bb3.bodybuddybe.post.entity.Post;
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

    public void createPost(PostCreateRequestDto postCreateRequestDto, UserDetailsImpl userDetails) {
        Gym gym = findGym(postCreateRequestDto.getGymId());

        Post post = Post.builder()
                .title(postCreateRequestDto.getTitle())
                .content(postCreateRequestDto.getContent())
                .category(postCreateRequestDto.getCategory())
                .imageUrl(postCreateRequestDto.getImageUrl())
                .videoUrl(postCreateRequestDto.getVideoUrl())
                .user(userDetails.getUser())
                .gym(gym)
                .build();

        postRepository.save(post);
    }

    public PostResponseDto getPostById(Long postId) {
        Post post = findPost(postId);

        return new PostResponseDto(post);
    }

    public PostListResponseDto getPostsByGymId(Long gymId) {
        List<PostResponseDto> postList = postRepository.findAllByGymId(gymId).stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postList);
    }


    @Transactional
    public void updatePost(Long postId, PostCreateRequestDto postCreateRequestDto, UserDetailsImpl userDetails) {
        Post post = findPost(postId);

        if (!post.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorCode.NOT_POST_WRITER);
        }

        post.update(postCreateRequestDto.getTitle(),
                postCreateRequestDto.getContent(),
                postCreateRequestDto.getCategory(),
                postCreateRequestDto.getImageUrl(),
                postCreateRequestDto.getVideoUrl());

        postRepository.save(post);
    }

    public void deletePost(Long postId, UserDetailsImpl userDetails) {
        Post post = findPost(postId);

        if (!post.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorCode.NOT_POST_WRITER);
        }

        postRepository.delete(post);
    }

    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );
    }
    //짐으로 변경
    public Gym findGym(Long id) {
        return gymRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND)
        );
    }
}
