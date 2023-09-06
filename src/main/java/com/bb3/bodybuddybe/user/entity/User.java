package com.bb3.bodybuddybe.user.entity;


import com.bb3.bodybuddybe.chat.entity.Message;
import com.bb3.bodybuddybe.chat.entity.UserChat;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.enums.AgeRangeEnum;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.dto.ChangedPasswordRequestDto;
import com.bb3.bodybuddybe.user.dto.ProfileUpdateRequestDto;
import com.bb3.bodybuddybe.user.dto.SocialUpdateInform;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import com.bb3.bodybuddybe.user.enums.UserStatusEnum;
import com.bb3.bodybuddybe.user.service.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private static final int AGE_20 = 20;
    private static final int AGE_30 = 30;
    private static final int AGE_40 = 40;
    private static final int AGE_50 = 50;
    private static final int AGE_60 = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;


    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    @Enumerated
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

    public void setGroupChatMemberList(List<UserChat> groupChatMemberList) {
        this.groupChatMemberList = groupChatMemberList;
    }

    public User(String email, String password, GenderEnum gender, LocalDate birthDate, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.birthDate = birthDate;
        this.role = role;
        this.status = UserStatusEnum.ACTIVE;
    }

    public void authorizeUser() {
        this.role = UserRoleEnum.USER;
    }

    public void updatePassword(String updatePassword) {
        this.password = updatePassword;
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

    public User updateSocialProfile(GenderEnum gender, LocalDate birthDate) {
        this.gender = gender;
        this.birthDate = birthDate;
        return this;
    }

    public AgeRangeEnum getAgeRange() {
        int age = getAge();

        if (age < AGE_20) return AgeRangeEnum.S10s;
        if (age < AGE_30) return AgeRangeEnum.S20s;
        if (age < AGE_40) return AgeRangeEnum.S30s;
        if (age < AGE_50) return AgeRangeEnum.S40s;
        if (age < AGE_60) return AgeRangeEnum.S50s;

        return AgeRangeEnum.S60plus;
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        int age = currentDate.getYear() - birthDate.getYear();

        if (currentDate.isBefore(birthDate.withYear(currentDate.getYear()))) {
            age--;  // 올해의 생일이 지났는지 확인
        }

        return age;
    }


}
