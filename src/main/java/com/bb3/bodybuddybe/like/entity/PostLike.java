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
@Table(name = "post_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostLike(User user, Post post) {
        this.user = user;
        this.post = post;
        user.getPostLikes().add(this);
        post.getLikes().add(this);
    }
}
