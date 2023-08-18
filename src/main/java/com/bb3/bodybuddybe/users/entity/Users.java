package com.bb3.bodybuddybe.users.entity;

import com.bb3.bodybuddybe.users.UsersBlockEnum;
import com.bb3.bodybuddybe.users.UsersRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.bb3.bodybuddybe.users.UsersBlockEnum.허가;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Users {
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
    private UsersRoleEnum role;


    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UsersBlockEnum status;

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


    public Users(String username, String nickname, String password, String passwordDecoded,String email, UsersRoleEnum role) {
        this.username=username;
        this.nickname=nickname;
        this.password=password;
        this.passwordDecoded = passwordDecoded;
        this.email=email;
        this.role=role;
        this.status = 허가;
    }
}
