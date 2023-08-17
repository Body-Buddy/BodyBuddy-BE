package com.bb3.bodybuddybe.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long cardId;
    private String username;
    private String description;
}
