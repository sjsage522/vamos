package io.wisoft.vamos.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.chatting.ChattingContent;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatContentDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("writer")
    private String writer;

    @JsonProperty("content")
    private String content;

    @Builder
    public ChatContentDto(ChattingContent source) {
        id = source.getId();
        writer = source.getWriter().getEmail();
        content = source.getContent();
    }
}
