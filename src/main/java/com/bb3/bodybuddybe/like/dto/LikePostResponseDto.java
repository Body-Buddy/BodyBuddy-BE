package com.bb3.bodybuddybe.like.dto;

import com.bb3.bodybuddybe.like.entity.LikePost;
import lombok.Getter;

@Getter
public class LikePostResponseDto {

    private String username;
    private Long postId;
    public LikePostResponseDto(LikePost likePost) {
        this.username = likePost.getUser().getUsername();
        this.postId = likePost.getPost().getId();
    }
}