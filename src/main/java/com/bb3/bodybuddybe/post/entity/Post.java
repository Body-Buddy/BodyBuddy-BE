package com.bb3.bodybuddybe.post.entity;

import com.bb3.bodybuddybe.common.timestamped.TimeStamped;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.user.entity.User;
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
    private String imageUrl;

    @Column
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @Builder
    public Post(String title, String content, String category, String imageUrl, String videoUrl, User user, Gym gym) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.user = user;
        this.gym = gym;
    }

    public void update(String title, String content, String category, String imageUrl, String videoUrl) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }
}
