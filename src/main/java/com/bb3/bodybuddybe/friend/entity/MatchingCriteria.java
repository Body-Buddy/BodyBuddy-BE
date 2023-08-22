package com.bb3.bodybuddybe.friend.entity;

import com.bb3.bodybuddybe.friend.enums.*;
import com.bb3.bodybuddybe.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "matching_criteria")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AgeRangeEnum ageRange;

    @Column(nullable = false)
    private Boolean preferSameGender;

    @Column(nullable = false)
    private Boolean preferSameAgeRange;

    @Column(nullable = false)
    private String goals;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExperienceEnum experience;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IntensityEnum intensity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExerciseTimeEnum exerciseTime;

    public void setGoals(Set<GoalEnum> goals) {
        this.goals = String.join(",",
                goals.stream()
                        .map(Enum::name)
                        .toList());
    }

    public Set<GoalEnum> getGoals() {
        return Arrays.stream(goals.split(","))
                .map(GoalEnum::valueOf)
                .collect(Collectors.toSet());
    }
}
