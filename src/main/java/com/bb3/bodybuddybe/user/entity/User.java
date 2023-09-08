package com.bb3.bodybuddybe.user.entity;

import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.entity.UserChat;
import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.enums.AgeRangeEnum;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.dto.ProfileUpdateRequestDto;
import com.bb3.bodybuddybe.user.dto.SocialSignupRequestDto;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import com.bb3.bodybuddybe.user.enums.UserStatusEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MatchingCriteria matchingCriteria;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserChat> groupChatMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserGym> userGymList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Message> messageList = new ArrayList<>();

    @Builder(builderMethodName = "basicSignupBuilder")
    public User(String email, String password, GenderEnum gender, LocalDate birthDate, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        this.role = role;
        this.status = UserStatusEnum.ACTIVE;
    }

    @Builder(builderMethodName = "socialSignupBuilder")
    public User(String email, String password, String nickname, String imageUrl, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = role;
        this.status = UserStatusEnum.ACTIVE;
    }

    public void updatePassword(String newPassword) {
        if(this.password.equals(newPassword)) {
            throw new CustomException(ErrorCode.SAME_PASSWORD);
        }
        this.password = newPassword;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateProfile(ProfileUpdateRequestDto requestDto) {
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
}
