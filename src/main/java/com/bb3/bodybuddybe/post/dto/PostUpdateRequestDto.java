package com.bb3.bodybuddybe.post.dto;

import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotBlank(message = "카테고리를 입력해주세요.")
    private CategoryEnum category;
}
