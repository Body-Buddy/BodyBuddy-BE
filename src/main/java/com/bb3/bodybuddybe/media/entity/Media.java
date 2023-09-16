package com.bb3.bodybuddybe.media.entity;

import com.bb3.bodybuddybe.media.enums.MediaTypeEnum;
import com.bb3.bodybuddybe.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "media")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    MediaTypeEnum mediaType;

    @Column(nullable = false, unique = true)
    private String s3Url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public Media(MediaTypeEnum mediaType, String s3Url, Post post) {
        this.mediaType = mediaType;
        this.s3Url = s3Url;
        this.post = post;
    }
}
