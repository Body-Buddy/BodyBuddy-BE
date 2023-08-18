package com.bb3.bodybuddybe.post.entity;

import com.bb3.bodybuddybe.common.timestamped.TimeStamped;
import com.bb3.bodybuddybe.post.dto.PostCreateRequestDto;
import com.bb3.bodybuddybe.users.entity.Users;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

//    @ManyToOne
//    @JoinColumn(name = "gym_id")
//    private gym gym;

//    @OneToMany(mappedBy = "Post", cascade = CascadeType.REMOVE)
//    private Comment comment;

    public Post(PostCreateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.category = requestDto.getCategory();
        this.image_url = requestDto.getImage_url();
        this.video_url = requestDto.getVideo_url();
    }

    public void setUser(Users users) {
        this.users = users;
    }
}
