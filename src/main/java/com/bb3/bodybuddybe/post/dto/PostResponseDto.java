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
    private String image_url;
    private String video_url;
    private LocalDateTime post_date;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.image_url = post.getImage_url();
        this.video_url = post.getVideo_url();
        this.post_date = post.getCreatedAt();
    }
}
