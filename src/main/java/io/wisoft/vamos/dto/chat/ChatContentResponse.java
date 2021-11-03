package io.wisoft.vamos.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.wisoft.vamos.domain.chatting.ChattingContent;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private Long writerId;

    @ApiModelProperty(
            value = "채팅 내용",
            name = "content",
            example = "content"
    )
    @JsonProperty("content")
    private String content;

    @ApiModelProperty(
            value = "채팅내용 생성 시간",
            name = "createdAt"
    )
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Builder
    public ChatContentResponse(ChattingContent source) {
        id = source.getId();
        writerId = source.getWriter().getId();
        content = source.getContent();
        createdAt = source.getCreatedAt();
    }
}
