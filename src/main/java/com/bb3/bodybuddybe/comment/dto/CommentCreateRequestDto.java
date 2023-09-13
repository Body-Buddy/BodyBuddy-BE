package com.bb3.bodybuddybe.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequestDto {

    @NotNull(message = "게시글 id를 입력해주세요.")
    private Long postId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    @NotNull(message = "부모 댓글 id를 입력해주세요. 최상위 댓글인 경우 부모 id는 0입니다.")
    private Long parentId;
}
