package com.clothz.aistyling.global.error;

public enum ErrorCode {
    //400에러
    USER_NOT_FOUND( "해당 유저를 찾을 수 없습니다."),
    MISSING_REQUIRED_PARAMETER("필수 파라미터가 없습니다"),

    //401에러
    INVALID_PARAMETER("파라미터가 잘못되었습니다"),
    NO_AUTH_TOKEN("인증 토큰이 없습니다"),
    INVALID_AUTH_TOKEN("인증 토큰이 유효하지 않습니다"),
    EXPIRED_AUTH_TOKEN("인증 토큰이 만료되었습니다"),
    FAILED_LOGIN("로그인에 실패하였습니다"),
    DUPLICATED_LOGIN("이미 로그인 중입니다"),
    INVALID_VERIFICATION_CODE("인증 코드가 유효하지 않습니다"),
    INVALID_DEVICE_TOKEN("디바이스 토큰이 유효하지 않습니다"),

    //403에러
    NOT_ALLOWED("허용되지 않은 접근입니다"),
    NOT_ADMIN_USER("관리자 권한이 없습니다"),

    //404에러
    NOT_FOUND("해당 리소스를 찾을 수 없습니다"),
    IMAGE_NOT_FOUND("해당 이미지를 찾을 수 없습니다"),

    //409에러
    DUPLICATED_USER("이미 존재하는 유저입니다"),

    //500에러
    INTERNAL_SERVER_ERROR("서버 내부 오류입니다");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
