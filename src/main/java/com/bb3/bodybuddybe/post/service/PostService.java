package com.bb3.bodybuddybe.post.service;

import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
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

    public void createPost(PostCreateRequestDto postCreateRequestDto, UserDetailsImpl userDetails) {

        Post post = Post.builder()
                .title(postCreateRequestDto.getTitle())
                .content(postCreateRequestDto.getContent())
                .category(postCreateRequestDto.getCategory())
                .image_url(postCreateRequestDto.getImage_url())
                .video_url(postCreateRequestDto.getVideo_url())
                .users(userDetails.getUser())
                .gym(postCreateRequestDto.getGymId())
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

        if (!post.getUsers().getId().equals(userDetails.getUser().getId())) {
            throw new RejectedExecutionException("게시글 생성자만 수정할 수 있습니다.");
        }

        post.update(postCreateRequestDto.getTitle(),
                postCreateRequestDto.getContent(),
                postCreateRequestDto.getCategory(),
                postCreateRequestDto.getImage_url(),
                postCreateRequestDto.getVideo_url());

        postRepository.save(post);
    }

    public void deletePost(Long postId, UserDetailsImpl userDetails) {
        Post post = findPost(postId);

        if (!post.getUsers().getId().equals(userDetails.getUser().getId())) {
            throw new RejectedExecutionException("게시글 생성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
    }
}
