package com.bb3.bodybuddybe.user.entity;

import com.bb3.bodybuddybe.chat.entity.GroupChatMember;
import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.enums.AgeRangeEnum;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.dto.ProfileUpdateRequestDto;
import com.bb3.bodybuddybe.user.enums.SocialType;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import com.bb3.bodybuddybe.user.enums.UserStatusEnum;
import com.bb3.bodybuddybe.user.service.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column
    private String nickname;

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
    private UserStatusEnum status;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MatchingCriteria matchingCriteria;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<GroupChatMember> groupChatMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserGym> userGymList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();

    public User(String username, String password, String email, String birthDate, GenderEnum gender, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.role = role;
        this.birthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.status = UserStatusEnum.ACTIVE;
    }

    public void authorizeUser() {
        this.role = UserRoleEnum.USER;
    }

    public void updateNickname(String updateNickname) {
        this.nickname = updateNickname;
    }

    public void updatePassword(String updatePassword) {
        this.password = updatePassword;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateProfile(ProfileUpdateRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.introduction = requestDto.getIntroduction();
    }

    public AgeRangeEnum getAgeRange() {
        int age = getAge();

        if (age < 20) {
            return AgeRangeEnum.S10s;
        } else if (age < 30) {
            return AgeRangeEnum.S20s;
        } else if (age < 40) {
            return AgeRangeEnum.S30s;
        } else if (age < 50) {
            return AgeRangeEnum.S40s;
        } else if (age < 60) {
            return AgeRangeEnum.S50s;
        } else {
            return AgeRangeEnum.S60plus;
        }
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        int age = currentDate.getYear() - birthDate.getYear();

        if (birthDate.getMonthValue() > currentDate.getMonthValue() ||
                (birthDate.getMonthValue() == currentDate.getMonthValue() &&
                        birthDate.getDayOfMonth() > currentDate.getDayOfMonth())) {
            age--;  // 생일이 아직 지나지 않은 경우를 고려함
        }

        return age;
    }
}
