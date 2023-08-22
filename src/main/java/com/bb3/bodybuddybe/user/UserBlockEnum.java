package com.bb3.bodybuddybe.user;

public enum UserBlockEnum {
    ACTIVE("허가"),
    INACTIVE("휴면"),
    BLOCKED("차단");


    private String userStatus;

    UserBlockEnum(String userStatus) {
        this.userStatus = userStatus;
    }
}
