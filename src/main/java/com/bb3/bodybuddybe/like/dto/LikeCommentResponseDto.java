package com.bb3.bodybuddybe.like.dto;

import com.bb3.bodybuddybe.like.entity.LikeComment;
import lombok.Getter;

@Getter
public class LikeCommentResponseDto {
    private String username;
    private Long commentId;


    public LikeCommentResponseDto(LikeComment likeComment) {
        this.username = likeComment.getUser().getUsername();
        this.commentId = likeComment.getComment().getId();
    }
}