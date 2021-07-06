package io.wisoft.vamos.common.jwt;

public enum ErrorCode {

    NON_TOKEN(401, "JWT001", "토큰이 존재하지 않습니다."),
    LOGOUT_TOKEN(401, "JWT002", "로그아웃된 토큰입니다."),
    MALFORMED_TOKEN(401, "JWT003", "잘못된 JWT 서명 입니다."),
    EXPIRED_TOKEN(401, "JWT004", "만료된 JWT 입니다."),
    UNSUPPORTED_TOKEN(401, "JWT005", "지원되지 않는 JWT 입니다."),
    ILLEGAL_TOKEN(401, "JWT006", "잘못된 JWT 입니다."),
    UNKNOWN_ACCESS(520, "U001", "알 수 없는 에러가 발생했습니다.")
    ;

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
