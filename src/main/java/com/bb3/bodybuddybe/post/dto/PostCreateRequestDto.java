package com.bb3.bodybuddybe.post.dto;

import lombok.Getter;

@Getter
public class PostCreateRequestDto {
    private String title;
    private String content;
    private String category;
    private String image_url;
    private String video_url;
}
