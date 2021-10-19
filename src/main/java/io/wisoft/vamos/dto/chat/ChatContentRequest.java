package io.wisoft.vamos.dto.chat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel("채팅 내용 요청")
public class ChatContentRequest {

    @ApiModelProperty(
            value = "채팅 작성자 이메일",
            name = "writerEmail",
            example = "tester@gmail.com",
            required = true
    )
    private String writerEmail;

    @ApiModelProperty(
            value = "채팅방 고유 id",
            name = "chatRoomId",
            example = "1",
            required = true
    )
    private Long chatRoomId;

    @ApiModelProperty(
            value = "채팅 내용",
            name = "content",
            example = "content",
            required = true
    )
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
