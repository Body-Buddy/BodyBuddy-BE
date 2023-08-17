package com.bb3.bodybuddybe.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponseDto {
    private List<PostListResponseDto> postsList;

    public PostListResponseDto(List<PostListResponseDto> postsList) {
        this.postsList = getPostsList();
    }
}
