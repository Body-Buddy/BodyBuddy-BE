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

    // chat
    CHAT_NOT_FOUND(HttpStatus.BAD_REQUEST, "C001", "존재하지 않는 채팅방 입니다."),
    CHAT_NOT_MY_GYM(HttpStatus.BAD_REQUEST, "C002", "사용자가 등록하지 않은 시설의 채팅방 입니다."),
    NEED_ENTER(HttpStatus.BAD_REQUEST, "C003", "ENTER 요청하여 채팅방 입장이 필요합니다."),

    // user
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U001", "존재하지 않는 사용자 입니다."),
    DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST, "U001", "중복된 사용자 아이디입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "U002", "이미 가입된 이메일입니다."),
    UNDER_AGE(HttpStatus.BAD_REQUEST, "U003", "만 14세 이상만 가입 가능합니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "U004", "비밀번호가 일치하지 않습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "U005", "이메일 인증번호가 유효하지 않습니다."),
    EMAIL_SENDING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "U006", "이메일 전송에 실패했습니다."),
    FAILED_TO_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "U007", "S3 파일 업로드에 실패했습니다."),
    INVALID_S3_URL(HttpStatus.INTERNAL_SERVER_ERROR, "U008", "유효하지 않은 S3 URL 입니다."),
    FAILED_TO_DELETE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "U009", "S3 파일 삭제에 실패했습니다."),
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
