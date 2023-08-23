package com.bb3.bodybuddybe.user.entity;

import com.bb3.bodybuddybe.chat.entity.Chat;
import com.bb3.bodybuddybe.chat.entity.GroupChatMember;
import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.user.UserBlockEnum;
import com.bb3.bodybuddybe.user.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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


    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<GroupChatMember> groupChatMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserGym> userGymList = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();


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
