package com.bb3.bodybuddybe.post.service;

import com.bb3.bodybuddybe.post.dto.PostCreateRequestDto;
import com.bb3.bodybuddybe.post.dto.PostListResponseDto;
import com.bb3.bodybuddybe.post.dto.PostResponseDto;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostResponseDto createPost(PostCreateRequestDto postCreateRequestDto, User user) {
        Post post = new Post(postCreateRequestDto);
        post.setUser(user);

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    public PostResponseDto getPostById(Long postId) {
        Post post = findPost(postId);

        return new PostResponseDto(post);
    }

    public PostListResponseDto getPostsByGymId(Long gymId) {


        return new PostListResponseDto();
    }
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
    }
}
