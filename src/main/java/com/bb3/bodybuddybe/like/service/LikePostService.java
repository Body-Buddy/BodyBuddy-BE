package com.bb3.bodybuddybe.like.service;

import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.like.dto.LikePostResponseDto;
import com.bb3.bodybuddybe.like.entity.LikePost;
import com.bb3.bodybuddybe.like.repository.LikePostRepository;
import com.bb3.bodybuddybe.notification.service.NotificationService;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.repository.PostRepository;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class LikePostService {

    private final LikePostRepository likePostRepository;
    private final PostRepository postRepository;
    private final MessageSource messageSource;
    private final NotificationService notificationService;
    public LikePostResponseDto save(UserDetailsImpl userDetails, Long id) {
        User user = userDetails.getUser();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage(
                                "not.found.post",
                                null,
                                "존재하지 않는 게시글입니다.",
                                Locale.getDefault()
                        )
                ));
        if (likePostRepository.findByUserAndPost(user, post).isPresent())
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "already.like.post",
                            null,
                            "이미 좋아요한 게시글입니다.",
                            Locale.getDefault()
                    )
            );

        LikePost likePost = new LikePost(post, user);

        notificationService.notifyToUsersThatTheyHaveReceivedLike(likePost);//좋아요 알림

        return new LikePostResponseDto(likePostRepository.save(likePost));
    }

    public Long delete(UserDetailsImpl userDetails, Long id) {
        User user = userDetails.getUser();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage(
                                "not.found.post",
                                null,
                                "존재하지 않는 게시글입니다.",
                                Locale.getDefault()
                        )
                ));
        LikePost likePost = likePostRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage(
                                "not.like.post",
                                null,
                                "좋아요를 누르지 않은 게시글입니다.",
                                Locale.getDefault()
                        )
                ));
        likePostRepository.delete(likePost);
        return id;
    }
}