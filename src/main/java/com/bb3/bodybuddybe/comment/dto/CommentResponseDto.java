package com.bb3.bodybuddybe.comment.dto;

import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.like.entity.CommentLike;
import com.bb3.bodybuddybe.user.dto.AuthorDto;
import com.bb3.bodybuddybe.user.dto.ProfileResponseDto;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long postId;
    private String content;
    private AuthorDto author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Long> likedUserIds;
    private List<CommentResponseDto> children;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.author = new AuthorDto(comment.getAuthor());
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likedUserIds = comment.getLikes()
                .stream()
                .map(CommentLike::getUser)
                .map(User::getId)
                .toList();
        this.children = comment.getChildren()
                .stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt))
                .toList();
    }
}

