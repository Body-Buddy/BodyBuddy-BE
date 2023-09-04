package com.bb3.bodybuddybe.like.dto;

import com.bb3.bodybuddybe.like.entity.LikePost;
import lombok.Getter;

@Getter
public class LikePostResponseDto {

    private String nickname;
    private Long postId;
    public LikePostResponseDto(LikePost likePost) {
        this.nickname = likePost.getUser().getNickname();
        this.postId = likePost.getPost().getId();
    }
}