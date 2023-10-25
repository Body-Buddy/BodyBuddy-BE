package com.bb3.bodybuddybe.post.dto;

import com.bb3.bodybuddybe.comment.dto.CommentResponseDto;
import com.bb3.bodybuddybe.like.entity.PostLike;
import com.bb3.bodybuddybe.media.dto.MediaDto;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import com.bb3.bodybuddybe.user.dto.AuthorDto;
import com.bb3.bodybuddybe.user.entity.User;
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
    private LocalDateTime modifiedAt;
    private AuthorDto author;
    private List<Long> likedUserIds;
    private List<CommentResponseDto> comments;
    private List<MediaDto> medias;

    public PostDetailResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.author = new AuthorDto(post.getAuthor());
        this.likedUserIds = post.getLikes()
                .stream()
                .map(PostLike::getUser)
                .map(User::getId)
                .toList();
        this.comments = post.getComments()
                .stream()
                .filter(comment -> comment.getParent() == null)
                .map(CommentResponseDto::new)
                .toList();
        this.medias = post.getMedias()
                .stream()
                .map(MediaDto::new)
                .toList();
    }
}
