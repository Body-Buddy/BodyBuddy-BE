package com.bb3.bodybuddybe.comment.dto;

import com.bb3.bodybuddybe.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponseDto{
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String nickname;
    private List<CommentResponseDto> commentList;

    public CommentResponseDto(Comment comment, String nickname) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.nickname = nickname;
    }

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.nickname = comment.getUser().getNickname();
        this.commentList = comment.getChild().stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}

