package com.bb3.bodybuddybe.post.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.common.image.ImageUploader;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.image.repository.ImageRepository;
import com.bb3.bodybuddybe.post.dto.*;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import com.bb3.bodybuddybe.post.repository.PostRepository;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final GymRepository gymRepository;
    private final ImageUploader imageUploader;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public void createPost(PostCreateRequestDto requestDto, User user) {
        Gym gym = findGym(requestDto.getGymId());

        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .category(requestDto.getCategory())
                .author(user)
                .gym(gym)
                .build();

        postRepository.save(post);

//        List<MultipartFile> files = new ArrayList<>();
//        files.addAll(requestDto.getImages());
//        files.addAll(requestDto.getVideos());
//
//        if (!files.isEmpty()) {
//            for (MultipartFile file : files) {
//                String s3Url = imageUploader.upload(file);
//                imageRepository.save(new Image(s3Url, post));
//            }
//        }
    }

    @Override
    @Transactional(readOnly = true)
    public PostDetailResponseDto getPostById(Long postId) {
        return new PostDetailResponseDto(findPost(postId));
    }

    @Override
    public List<CategoryResponseDto> getCategories() {
        return Arrays.stream(CategoryEnum.values())
                .map(CategoryResponseDto::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostSummaryResponseDto> getPostsByCategory(CategoryEnum category, Pageable pageable) {
        return postRepository.findAllByCategory(category, pageable)
                .map(PostSummaryResponseDto::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostSummaryResponseDto> getPostsByGymId(Long gymId, Pageable pageable) {
        return postRepository.findAllByGym(findGym(gymId), pageable)
                .map(PostSummaryResponseDto::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostSummaryResponseDto> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable)
                .map(PostSummaryResponseDto::new);
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
                new CustomException(ErrorCode.POST_NOT_FOUND)
        );
    }

    private Gym findGym(Long id) {
        return gymRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.GYM_NOT_FOUND)
        );
    }

    private void validatePostOwner(Post post, User user) {
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NOT_POST_AUTHOR);
        }
    }
}
