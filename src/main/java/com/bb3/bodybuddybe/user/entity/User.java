package com.bb3.bodybuddybe.user.entity;

import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.entity.UserChat;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.common.oauth2.dto.OAuthAttributes;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.like.entity.CommentLike;
import com.bb3.bodybuddybe.like.entity.PostLike;
import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.enums.AgeRangeEnum;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.dto.ProfileRequestDto;
import com.bb3.bodybuddybe.user.dto.SignupRequestDto;
import com.bb3.bodybuddybe.user.dto.SocialSignupRequestDto;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import com.bb3.bodybuddybe.user.enums.UserStatusEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    @Enumerated(value = EnumType.STRING)
    private GenderEnum gender;

    @Column
    private LocalDate birthDate;

    @Column
    private String imageUrl;

    @Column
    private String introduction;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserStatusEnum status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MatchingCriteria matchingCriteria;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserChat> groupChatMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserGym> userGymList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();

    @Column
    private Boolean needSocialSignup = true;

    @Column
    private Boolean hasRegisteredGym = false;

    @Column
    private Boolean hasSetProfile = false;

    @Column
    private Boolean hasSetMatchingCriteria = false;

    public User(SignupRequestDto requestDto) {
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.gender = requestDto.getGender();
        this.birthDate = requestDto.getBirthDate();
        this.needSocialSignup = false;
        this.role = UserRoleEnum.USER;
        this.status = UserStatusEnum.ACTIVE;
    }

    public User(OAuthAttributes attributes) {
        this.email = attributes.getEmail();
        this.password = UUID.randomUUID().toString();
        this.nickname = attributes.getName();
        this.imageUrl = attributes.getPicture();
        this.needSocialSignup = true;
        this.role = UserRoleEnum.USER;
        this.status = UserStatusEnum.ACTIVE;
    }

    public void updatePassword(String newPassword) {
        if (this.password.equals(newPassword)) {
            throw new CustomException(ErrorCode.SAME_PASSWORD);
        }
        this.password = newPassword;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setProfile(ProfileRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.introduction = requestDto.getIntroduction();
    }

    public User update(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.imageUrl = profileImageUrl;
        return this;
    }

    public void socialSignup(SocialSignupRequestDto requestDto) {
        this.gender = requestDto.getGender();
        this.birthDate = requestDto.getBirthDate();
    }

    public AgeRangeEnum getAgeRange() {
        int age = getAge();

        if (age < 20) return AgeRangeEnum.S10s;
        if (age < 30) return AgeRangeEnum.S20s;
        if (age < 40) return AgeRangeEnum.S30s;
        if (age < 50) return AgeRangeEnum.S40s;
        if (age < 60) return AgeRangeEnum.S50s;
        return AgeRangeEnum.S60plus;
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        int age = currentDate.getYear() - birthDate.getYear();

        if (currentDate.isBefore(birthDate.withYear(currentDate.getYear()))) {
            age--;
        }

        return age;
    }

    public void markedAsRegisteredGym() {
        this.hasRegisteredGym = true;
    }

    public void markedAsSetProfile() {
        this.hasSetProfile = true;
    }

    public void markedAsSetMatchingCriteria() {
        this.hasSetMatchingCriteria = true;
    }

    public void markedAsFinishedSignup() {
        this.needSocialSignup = false;
    }
}
