package com.bb3.bodybuddybe.post.dto;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponseDto extends ApiResponseDto {
    private String title;
    private String content;
    private String category;
    private String imageUrl;
    private String videoUrl;
    private LocalDateTime postDate;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.imageUrl = post.getImageUrl();
        this.videoUrl = post.getVideoUrl();
        this.postDate = post.getCreatedAt();
    }
}
