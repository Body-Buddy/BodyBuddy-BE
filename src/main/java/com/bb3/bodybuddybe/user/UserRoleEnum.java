package com.bb3.bodybuddybe.user;

public enum UserRoleEnum {
    USER(Authority.USER),
    CHATOWNER(Authority.CHATOWNER),
    POSTOWNER(Authority.POSTOWNER),
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
        public static final String OWNER = "ROLE_OWNER";
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String CHATOWNER = "ROLE_CHATOWNER";
        public static final String POSTOWNER = "ROLE_POSTOWNER";
    }
}


