package io.wisoft.vamos.dto.chat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatContentRequest {

    private String writerEmail;

    private Long chatRoomId;

    private String content;

    @Builder
    public ChatContentRequest(String writerEmail, Long chatRoomId, String content) {
        this.writerEmail = writerEmail;
        this.chatRoomId = chatRoomId;
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatContentRequest{" +
                "writerEmail='" + writerEmail + '\'' +
                ", chatRoomId=" + chatRoomId +
                ", content='" + content + '\'' +
                '}';
    }
}
