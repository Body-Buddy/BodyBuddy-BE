package com.bb3.bodybuddybe.post.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.post.dto.PostCreateRequestDto;
import com.bb3.bodybuddybe.post.dto.PostResponseDto;
import com.bb3.bodybuddybe.post.dto.PostUpdateRequestDto;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import com.bb3.bodybuddybe.post.repository.PostRepository;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl {
    private final PostRepository postRepository;
    private final GymRepository gymRepository;

    @Transactional
    public void createPost(PostCreateRequestDto requestDto, User user) {
        Gym gym = findGym(requestDto.getGymId());

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .user(user)
                .gym(gym)
                .build();

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long postId) {
        return new PostResponseDto(findPost(postId));
    }

    public List<CategoryEnum> getAllCategories() {
        return List.of(CategoryEnum.values());
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPostsByCategory(CategoryEnum category, Pageable pageable) {
        return postRepository.findAllByCategory(category, pageable)
                .map(PostResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPostsByGymId(Long gymId, Pageable pageable) {
        return postRepository.findAllByGym(findGym(gymId), pageable)
                .map(PostResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable)
                .map(PostResponseDto::new);
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateRequestDto requestDto, User user) {
        Post post = findPost(postId);
        validatePostOwner(post, user);
        post.update(requestDto);
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);
        validatePostOwner(post, user);
        postRepository.delete(post);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_POST)
        );
    }

    private Gym findGym(Long id) {
        return gymRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_GYM)
        );
    }

    private void validatePostOwner(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NOT_POST_WRITER);
        }
    }
}
