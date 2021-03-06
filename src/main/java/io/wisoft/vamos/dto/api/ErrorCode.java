package io.wisoft.vamos.dto.api;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOMATCH_BOARD_INFO(400,"B001", "존재하지 않는 게시글 입니다."),

    NOMATCH_CATEGORY_INFO(400, "CT001", "존재하지 않는 카테고리 입니다."),
    NOMATCH_COMMENT_INFO(400, "CM001", "존재하지 않는 답글 입니다."),

    NOMATCH_USER_INFO(400, "U001", "존재하지 않는 사용자 입니다."),
    NOT_SET_USER_NICKNAME(400, "U002", "사용자 별명이 설정되어 있지 않습니다."),
    NOT_SET_USER_LOCATION(400, "U003", "사용자 위치정보가 설정되어 있지 않습니다."),

    NOMATCH_CHAT_ROOM_INFO(400, "CR001", "존재하지 않는 채팅방 입니다."),

    INCORRECT_FORMAT(400, "C001", "잘못된 형식입니다."),
    INCORRECT_SERVLET_REQUEST(400, "C002", "잘못된 서블릿 요청입니다."),
    INCORRECT_HTTP_BODY_FORMAT(400, "C003", "잘못된 HTTP BODY 요청 형식입니다."),
    ILLEGAL_ARGUMENT(400, "C004", "잘못된 매개변수입니다."),
    HANDLER_NOT_FOUND(404, "C005", "요청 주소가 잘못되었습니다."),
    METHOD_NOT_ALLOWED(405, "C006", "지원하는 않는 요청 메서드 입니다."),
    UNSUPPORTED_MEDIA_TYPE(415, "C007", "지원하지 않는 미디어 타입 입니다."),

    AUTHENTICATION_INFO(401, "A001", "인증정보가 존재하지 않습니다."),
    AUTHORIZATION_INFO(403, "A002", "권한이 필요합니다."),

    INTERNAL_SERVER_ERROR(500, "I001", "내부 서버 에러.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
