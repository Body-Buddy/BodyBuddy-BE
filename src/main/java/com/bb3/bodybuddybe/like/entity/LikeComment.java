package com.bb3.bodybuddybe.like.entity;

import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class LikeComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public LikeComment(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}
