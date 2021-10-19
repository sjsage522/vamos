package io.wisoft.vamos.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.wisoft.vamos.domain.chatting.ChattingContent;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel("채팅 내용 공통 응답")
public class ChatContentResponse {

    @ApiModelProperty(
            value = "채팅 내용 고유 id",
            name = "id",
            example = "1"
    )
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(
            value = "채팅 작성자",
            name = "writer",
            example = "tester"
    )
    @JsonProperty("writer")
    private String writer;

    @ApiModelProperty(
            value = "채팅 내용",
            name = "content",
            example = "content"
    )
    @JsonProperty("content")
    private String content;

    @Builder
    public ChatContentResponse(ChattingContent source) {
        id = source.getId();
        writer = source.getWriter().getEmail();
        content = source.getContent();
    }
}
