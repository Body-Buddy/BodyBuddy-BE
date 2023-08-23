package com.bb3.bodybuddybe.matching.entity;

import com.bb3.bodybuddybe.matching.dto.CriteriaCreateRequestDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaUpdateRequestDto;
import com.bb3.bodybuddybe.matching.enums.ExerciseTimeEnum;
import com.bb3.bodybuddybe.matching.enums.ExperienceEnum;
import com.bb3.bodybuddybe.matching.enums.GoalEnum;
import com.bb3.bodybuddybe.matching.enums.IntensityEnum;
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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

    public MatchingCriteria(User user, CriteriaCreateRequestDto requestDto) {
        this.user = user;
        this.preferSameGender = requestDto.getPreferSameGender();
        this.preferSameAgeRange = requestDto.getPreferSameAgeRange();
        this.experience = requestDto.getExperience();
        this.intensity = requestDto.getIntensity();
        this.exerciseTime = requestDto.getExerciseTime();
        this.goals = String.join(",",
                requestDto.getGoals()
                        .stream()
                        .map(Enum::name)
                        .toList());
    }

    public void update(CriteriaUpdateRequestDto requestDto) {
        this.preferSameGender = requestDto.getPreferSameGender();
        this.preferSameAgeRange = requestDto.getPreferSameAgeRange();
        this.experience = requestDto.getExperience();
        this.intensity = requestDto.getIntensity();
        this.exerciseTime = requestDto.getExerciseTime();
        this.goals = String.join(",",
                requestDto.getGoals()
                        .stream()
                        .map(Enum::name)
                        .toList());
    }

    public Set<GoalEnum> getGoals() {
        return Arrays.stream(goals.split(","))
                .map(GoalEnum::valueOf)
                .collect(Collectors.toSet());
    }
}
