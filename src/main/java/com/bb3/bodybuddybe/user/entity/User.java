package com.bb3.bodybuddybe.user.entity;

import com.bb3.bodybuddybe.user.UserBlockEnum;
import com.bb3.bodybuddybe.user.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.bb3.bodybuddybe.user.UserBlockEnum.ACTIVE;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String passwordDecoded;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String imageUrl;

    @Column
    private String introduction;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserBlockEnum status;

//    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
//    private List<Notification> notificationList = new ArrayList<>();
//    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
//    private List<Comment> commentList = new ArrayList<>();
//    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
//    private List<Post> postList = new ArrayList<>();
//    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST, orphanRemoval = true)
//    private List<UserGym> userGymList = new ArrayList<>();
//    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
//    private List<Message> messageList = new ArrayList<>();
//    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
//    private List<GroupChatMember> groupChatMemberList = new ArrayList<>();


    public User(String username, String nickname, String password, String passwordDecoded, String email, UserRoleEnum role) {
        this.username=username;
        this.nickname=nickname;
        this.password=password;
        this.passwordDecoded = passwordDecoded;
        this.email=email;
         this.role=role;
        this.status = ACTIVE;
    }
}
