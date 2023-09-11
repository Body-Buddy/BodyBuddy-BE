package com.bb3.bodybuddybe.post.dto;

import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import com.bb3.bodybuddybe.user.dto.ProfileResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostSummaryResponseDto {
    private Long id;
    private String title;
    private String content;
    private CategoryEnum category;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private ProfileResponseDto author;
    private Integer likeCount;
    private Integer commentCount;

    public PostSummaryResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.author = new ProfileResponseDto(post.getAuthor());
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
    }
}
