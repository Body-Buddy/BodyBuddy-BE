package com.bb3.bodybuddybe.like.entity;

import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comment_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
        user.getCommentLikes().add(this);
        comment.getLikes().add(this);
    }
}
