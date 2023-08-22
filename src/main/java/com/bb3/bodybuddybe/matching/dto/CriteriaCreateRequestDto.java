package com.bb3.bodybuddybe.matching.dto;

import com.bb3.bodybuddybe.matching.enums.ExerciseTimeEnum;
import com.bb3.bodybuddybe.matching.enums.ExperienceEnum;
import com.bb3.bodybuddybe.matching.enums.GoalEnum;
import com.bb3.bodybuddybe.matching.enums.IntensityEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaCreateRequestDto {
    @NotNull
    private Boolean preferSameGender;

    @NotNull
    private Boolean preferSameAgeRange;

    @NotNull
    private Set<GoalEnum> goals;

    @NotNull
    private ExperienceEnum experience;

    @NotNull
    private IntensityEnum intensity;

    @NotNull
    private ExerciseTimeEnum exerciseTime;
}
