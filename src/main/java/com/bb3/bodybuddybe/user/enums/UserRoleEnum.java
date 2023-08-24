package com.bb3.bodybuddybe.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum UserRoleEnum {
    USER(Authority.USER),
    CHAT_OWNER(Authority.CHAT_OWNER),
    POST_OWNER(Authority.POST_OWNER),
    ADMIN(Authority.ADMIN);


    private final String authority;


    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String CHAT_OWNER = "ROLE_CHATOWNER";
        public static final String POST_OWNER = "ROLE_POSTOWNER";
    }


}


