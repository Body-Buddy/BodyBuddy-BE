package com.bb3.bodybuddybe.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequestDto {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;
}
