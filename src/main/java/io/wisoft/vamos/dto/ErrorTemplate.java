package io.wisoft.vamos.dto;

import lombok.Getter;

@Getter
public class ErrorTemplate {
    private String message;
    private Integer status;

    private ErrorTemplate(String message, Integer status) {
        if (message == null || message.isEmpty()) throw new IllegalArgumentException("에러 내용이 필요합니다.");
        if (status == null) throw new IllegalArgumentException("상태코드를 설정해 주세요.");
        this.message = message;
        this.status = status;
    }

    public static ErrorTemplate from(String message, Integer status) {
        return new ErrorTemplate(message, status);
    }

    @Override
    public String toString() {
        return "ErrorTemplate{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
