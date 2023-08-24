package com.bb3.bodybuddybe.user.entity;

import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.enums.AgeRangeEnum;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.enums.UserBlockEnum;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.bb3.bodybuddybe.user.enums.UserBlockEnum.허가;

@Entity
@NoArgsConstructor
@Getter
@Setter
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

    @Column
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column
    @Enumerated(EnumType.STRING)
    private AgeRangeEnum ageRange;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserBlockEnum status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MatchingCriteria matchingCriteria;

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
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.passwordDecoded = passwordDecoded;
        this.email = email;
        this.role = role;
        this.status = 허가;
    }
}
