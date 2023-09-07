package com.bb3.bodybuddybe.post.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.common.image.ImageUploader;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.image.entity.Image;
import com.bb3.bodybuddybe.image.repository.ImageRepository;
import com.bb3.bodybuddybe.post.dto.CategoryResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final GymRepository gymRepository;
    private final ImageUploader imageUploader;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public void createPost(PostCreateRequestDto requestDto, User user, List<MultipartFile> files) {
        Gym gym = findGym(requestDto.getGymId());

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .user(user)
                .gym(gym)
                .build();

        postRepository.save(post);
        if (files != null) {
            for (MultipartFile file : files) {
                String fileUrl = imageUploader.upload(file);
                if (imageRepository.existsByImageUrlAndId(fileUrl, post.getId())) {
                    throw new CustomException(ErrorCode.FILE_ALREADY_EXISTS);
                }
                imageRepository.save(Image.builder().imageUrl(fileUrl).post(post).build());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long postId) {
        return new PostResponseDto(findPost(postId));
    }

    @Override
    public List<CategoryResponseDto> getCategories() {
        return Arrays.stream(CategoryEnum.values())
                .map(CategoryResponseDto::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPostsByCategory(CategoryEnum category, Pageable pageable) {
        return postRepository.findAllByCategory(category, pageable)
                .map(PostResponseDto::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getPostsByGymId(Long gymId, Pageable pageable) {
        return postRepository.findAllByGym(findGym(gymId), pageable)
                .map(PostResponseDto::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable)
                .map(PostResponseDto::new);
    }

    @Override
    @Transactional
    public void updatePost(Long postId, PostUpdateRequestDto requestDto, User user) {
        Post post = findPost(postId);
        validatePostOwner(post, user);
        post.update(requestDto);
    }

    @Override
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
