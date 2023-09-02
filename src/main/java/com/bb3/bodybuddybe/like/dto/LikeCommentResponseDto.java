package com.bb3.bodybuddybe.like.dto;

import com.bb3.bodybuddybe.like.entity.LikeComment;
import lombok.Getter;

@Getter
public class LikeCommentResponseDto {
    private String nickname;
    private Long commentId;


    public LikeCommentResponseDto(LikeComment likeComment) {
        this.nickname = likeComment.getUser().getNickname();
        this.commentId = likeComment.getComment().getId();
    }
}