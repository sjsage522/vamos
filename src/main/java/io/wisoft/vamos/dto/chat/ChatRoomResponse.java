package io.wisoft.vamos.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.wisoft.vamos.domain.chatting.ChattingRoom;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ApiModel("채팅방 공통 응답")
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
    private String seller;

    @ApiModelProperty(
            value = "구매자 이메일",
            name = "buyer",
            example = "buyer@gmail.com"
    )
    @JsonProperty("buyer")
    private String buyer;

    @ApiModelProperty(
            value = "채팅 기록들",
            name = "chatting_info"
    )
    @JsonProperty("chatting_info")
    private List<ChatContentResponse> chatContentResponse = new ArrayList<>();

    @Builder
    public ChatRoomResponse(ChattingRoom source) {
        id = source.getId();
        seller = source.getSeller().getEmail();
        buyer = source.getBuyer().getEmail();
        chatContentResponse = source.getChattingContent()
                .stream()
                .map(ChatContentResponse::new)
                .collect(Collectors.toList());
    }
}
