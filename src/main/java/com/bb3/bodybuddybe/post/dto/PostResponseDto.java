package com.bb3.bodybuddybe.post.dto;

import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponseDto {
    private String title;
    private String content;
    private CategoryEnum category;
    private LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt();
    }
}
