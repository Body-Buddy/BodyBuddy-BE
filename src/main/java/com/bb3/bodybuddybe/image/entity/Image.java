package com.bb3.bodybuddybe.image.entity;

import com.bb3.bodybuddybe.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String s3Url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Image(String s3Url, Post post) {
        this.s3Url = s3Url;
        this.post = post;
    }
}
