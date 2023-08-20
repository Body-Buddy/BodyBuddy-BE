package com.bb3.bodybuddybe.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // gym
    GYM_NOT_FOUND(HttpStatus.BAD_REQUEST, "G001", "존재하지 않는 헬스장입니다."),
    NOT_MY_GYM(HttpStatus.BAD_REQUEST, "G002", "사용자가 등록한 헬스장이 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
