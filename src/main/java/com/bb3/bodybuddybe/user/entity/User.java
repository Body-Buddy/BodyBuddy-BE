package com.bb3.bodybuddybe.user.entity;

import com.bb3.bodybuddybe.chat.entity.GroupChatMember;
import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.gym.entity.UserGym;

import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.enums.AgeRangeEnum;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.enums.UserBlockEnum;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import com.bb3.bodybuddybe.user.enums.SocialType;
import com.bb3.bodybuddybe.user.service.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static com.bb3.bodybuddybe.user.enums.UserBlockEnum.ACTIVE;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
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
    private Role socialRole;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserBlockEnum status;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰

    @Column
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column
    @Enumerated(EnumType.STRING)
    private AgeRangeEnum ageRange;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MatchingCriteria matchingCriteria;

    // 유저 권한 설정 메소드
    public void authorizeUser() {
        this.role = UserRoleEnum.USER;
    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<GroupChatMember> groupChatMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserGym> userGymList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();


    // 유저 권한 설정 메소드


    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    //== 유저 필드 업데이트 ==//
    public void updateNickname(String updateNickname) {
        this.nickname = updateNickname;
    }



    public void updatePassword(String updatePassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(updatePassword);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }


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
