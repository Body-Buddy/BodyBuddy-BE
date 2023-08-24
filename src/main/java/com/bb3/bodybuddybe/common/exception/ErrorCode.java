package com.bb3.bodybuddybe.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // gym
    GYM_NOT_FOUND(HttpStatus.BAD_REQUEST, "G001", "존재하지 않는 헬스장입니다."),
    NOT_MY_GYM(HttpStatus.BAD_REQUEST, "G002", "사용자가 등록한 헬스장이 아닙니다."),
    DUPLICATED_MY_GYM(HttpStatus.BAD_REQUEST, "G003", "이미 사용자가 등록한 헬스장입니다."),

    // post
    NOT_POST_WRITER(HttpStatus.BAD_REQUEST,"P001" ,"게시글 생성자만 수정할 수 있습니다." ),
    NOT_FOUND(HttpStatus.BAD_REQUEST,"P002","게시글이 존재하지 않습니다."),

    // matching
    MATCHING_CRITERIA_NOT_FOUND(HttpStatus.BAD_REQUEST, "M001", "사용자의 매칭 기준이 존재하지 않습니다."),
    CHAT_NOT_FOUND(HttpStatus.BAD_REQUEST, "G004", "존재하지 않는 채팅방 입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "G005", "존재하지 않는 사용자 입니다."),
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
