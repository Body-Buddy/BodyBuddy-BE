package com.bb3.bodybuddybe.post.dto;

import com.bb3.bodybuddybe.comment.dto.CommentResponseDto;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import com.bb3.bodybuddybe.user.dto.ProfileResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private CategoryEnum category;
    private LocalDateTime createdAt;
    private ProfileResponseDto author;
    private Integer likeCount;
    private List<CommentResponseDto> comments;
    // media files

    public PostDetailResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt();
        this.author = new ProfileResponseDto(post.getAuthor());
        this.likeCount = post.getLikeCount();
        this.comments = post.getComments()
                .stream()
                .map(CommentResponseDto::new)
                .toList();
    }
}
