package com.bb3.bodybuddybe.post.dto;

import com.bb3.bodybuddybe.gym.entity.Gym;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {
    private String title;
    private String content;
    private String category;
    private String imageUrl;
    private String videoUrl;
    private Long gymId;
}
