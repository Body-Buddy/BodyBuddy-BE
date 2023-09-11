package com.bb3.bodybuddybe.comment.dto;

import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.user.dto.ProfileResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private ProfileResponseDto author;
    private List<CommentResponseDto> childComments;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.author = new ProfileResponseDto(comment.getAuthor());
        this.childComments = comment.getChild()
                .stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}

