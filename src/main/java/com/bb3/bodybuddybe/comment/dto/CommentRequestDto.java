package com.bb3.bodybuddybe.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long postId;
    private Long parentId;
    private String content;
}
