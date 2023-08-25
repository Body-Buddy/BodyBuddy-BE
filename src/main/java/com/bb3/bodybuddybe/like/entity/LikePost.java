package com.bb3.bodybuddybe.like.entity;

import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class LikePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public LikePost(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}