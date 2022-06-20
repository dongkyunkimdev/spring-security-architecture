package com.dkkim.springsecurity.exception.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "유효하지 않은 파라미터 값입니다"),
    INVALID_TYPE_VALUE(400, "C002", "유효하지 않은 파라미터 타입입니다"),
    MISSING_VALUE(400, "C003", "필수 파라미터가 존재하지 않습니다"),
    MESSAGE_NOT_READABLE(400, "C004", "유효하지 않은 포맷입니다"),
    ENTITY_NOT_FOUND(404, "C005", "결과가 존재하지 않습니다"),
    METHOD_NOT_ALLOWED(405, "C006", "허용되지 않은 메서드입니다"),
    HANDLE_ACCESS_DENIED(403, "C007", "권한이 존재하지 않습니다"),
    INTERNAL_SERVER_ERROR(500, "C008", "Server Error"),

    // Authentication
    INVALID_TOKEN(401, "A001", "유효하지 않은 JWT 토큰입니다"),
    EXPIRED_TOKEN(401, "A002", "만료된 JWT 토큰입니다"),
    UNSUPPORTED_TOKEN(401, "A003", "지원되지 않는 JWT 토큰입니다"),
    INCORRECT_SIGNATURE(401, "A004", "유효하지 않은 JWT 서명입니다"),
    UNKNOWN_TOKEN(401, "A005", "알 수 없는 인증 오류입니다"),
    ;

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

