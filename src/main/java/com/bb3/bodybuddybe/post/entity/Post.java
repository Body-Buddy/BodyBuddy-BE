package com.bb3.bodybuddybe.post.entity;

import com.bb3.bodybuddybe.common.timestamped.TimeStamped;
import com.bb3.bodybuddybe.users.entity.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String category;

    @Column
    private String image_url;

    @Column
    private String video_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

//    @ManyToOne
//    @JoinColumn(name = "gym_id")
//    private gym gym;

//    @OneToMany(mappedBy = "Post", cascade = CascadeType.REMOVE)
//    private Comment comment;

    @Builder
    public Post(String title, String content, String category, String image_url, String video_url, Users users) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.image_url = image_url;
        this.video_url = video_url;
        this.users = users;
    }

    public void update(String title, String content, String category, String imageUrl, String videoUrl) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.image_url = imageUrl;
        this.video_url = videoUrl;
    }
}
