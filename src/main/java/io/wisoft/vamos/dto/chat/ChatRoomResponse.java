package io.wisoft.vamos.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.wisoft.vamos.domain.chatting.ChattingRoom;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomResponse {
    @ApiModelProperty(
            value = "채팅방 고유 id",
            name = "id",
            example = "1"
    )
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(
            value = "판매자 이메일",
            name = "seller",
            example = "seller@gmail.com"
    )
    @JsonProperty("seller")
    private Long sellerId;

    @ApiModelProperty(
            value = "구매자 이메일",
            name = "buyer",
            example = "buyer@gmail.com"
    )
    @JsonProperty("buyer")
    private Long buyerId;

    @ApiModelProperty(
            value = "채팅 기록들",
            name = "chatting_info"
    )
    @JsonProperty("chatting_info")
    private List<ChatContentResponse> chatContentResponse;

    @ApiModelProperty(
            value = "채팅방 생성 시간",
            name = "createdAt"
    )
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Builder
    public ChatRoomResponse(ChattingRoom source) {
        id = source.getId();
        sellerId = source.getSeller().getId();
        buyerId = source.getBuyer().getId();

        //null 일경우 채팅방 최초생성
        chatContentResponse = source.getChattingContents() != null ? source.getChattingContents()
                .stream()
                .map(ChatContentResponse::new)
                .collect(Collectors.toList()) : null;

        createdAt = source.getCreatedAt();
    }
}
