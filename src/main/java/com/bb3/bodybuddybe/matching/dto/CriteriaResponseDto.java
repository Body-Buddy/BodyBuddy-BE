package com.bb3.bodybuddybe.matching.dto;

import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.enums.ExerciseTimeEnum;
import com.bb3.bodybuddybe.matching.enums.ExperienceEnum;
import com.bb3.bodybuddybe.matching.enums.GoalEnum;
import com.bb3.bodybuddybe.matching.enums.IntensityEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
public class CriteriaResponseDto {
    private Long id;
    private Boolean preferSameGender;
    private Boolean preferSameAgeRange;
    private Set<GoalEnum> goals;
    private ExperienceEnum experience;
    private IntensityEnum intensity;
    private ExerciseTimeEnum exerciseTime;

    public CriteriaResponseDto(MatchingCriteria criteria) {
        this.id = criteria.getId();
        this.preferSameGender = criteria.getPreferSameGender();
        this.preferSameAgeRange = criteria.getPreferSameAgeRange();
        this.goals = criteria.getGoals();
        this.experience = criteria.getExperience();
        this.intensity = criteria.getIntensity();
        this.exerciseTime = criteria.getExerciseTime();
    }
}
