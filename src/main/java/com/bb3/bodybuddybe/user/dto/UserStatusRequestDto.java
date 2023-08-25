package com.bb3.bodybuddybe.user.dto;

import com.bb3.bodybuddybe.user.enums.UserStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotNull(message = "변경하려는 상태를 입력해주세요.")
    private UserStatusEnum status;
}
