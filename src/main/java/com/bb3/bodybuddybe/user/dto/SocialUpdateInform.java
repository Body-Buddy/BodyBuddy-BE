package com.bb3.bodybuddybe.user.dto;

import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class SocialUpdateInform {
    @NotNull(message = "성별을 선택해주세요.")
    private GenderEnum gender;

    @NotNull(message = "생년월일을 입력해주세요.")
    @Past(message = "생년월일은 오늘 날짜보다 이전이어야 합니다.")
    private LocalDate birthDate;
}
