package com.bb3.bodybuddybe.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    GUEST("ROLE_GUEST"), USER("ROLE_USER");
    //소셜 첫 로그인 GUEST, 이후 사용자 정보 추가 입력 USER

    private final String key;
}
