package com.bb3.bodybuddybe.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostCategoryListResponseDto {
    private List<PostResponseDto> postCategoryList;

    public PostCategoryListResponseDto(List<PostResponseDto> postCategoryList) {
        this.postCategoryList = postCategoryList;
    }
}
