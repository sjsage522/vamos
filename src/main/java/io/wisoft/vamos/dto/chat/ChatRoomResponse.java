package io.wisoft.vamos.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.chatting.ChattingRoom;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ChatRoomResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("seller")
    private String seller;

    @JsonProperty("buyer")
    private String buyer;

    @JsonProperty("chatting_info")
    private List<ChatContentDto> chatContentDto = new ArrayList<>();

    @Builder
    public ChatRoomResponse(ChattingRoom source) {
        id = source.getId();
        seller = source.getSeller().getEmail();
        buyer = source.getBuyer().getEmail();
        chatContentDto = source.getChattingContent()
                .stream()
                .map(ChatContentDto::new)
                .collect(Collectors.toList());
    }
}
