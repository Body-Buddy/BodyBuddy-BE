package com.bb3.bodybuddybe.post.entity;

import com.bb3.bodybuddybe.common.timestamped.TimeStamped;
import jakarta.persistence.*;
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

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

//    @ManyToOne
//    @JoinColumn(name = "gym_id")
//    private gym gym;

//    @OneToMany(mappedBy = "Post", cascade = CascadeType.REMOVE)
//    private Comment comment;

    public Post(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.image_url = post.getImage_url();
        this.video_url = post.getVideo_url();
    }
}
