package io.wisoft.vamos.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.wisoft.vamos.domain.chatting.ChattingContent;
import io.wisoft.vamos.domain.chatting.ChattingRoom;
import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.chat.ChatContentRequest;
import io.wisoft.vamos.dto.chat.ChatRoomResponse;
import io.wisoft.vamos.security.UserPrincipal;
import io.wisoft.vamos.service.BoardService;
import io.wisoft.vamos.service.ChatService;
import io.wisoft.vamos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

import static io.wisoft.vamos.dto.api.ApiResult.succeed;

@RequiredArgsConstructor
@Api(tags = "ChatController")
@RestController
@RequestMapping("api")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final BoardService boardService;
    private final UserService userService;

    /**
     * 채팅으로 거래하기
     *
     * @param boardId       게시글 고유 id
     * @param userPrincipal 현재 로그인 사용자
     * @return 채팅방 정보
     */
    @ApiOperation(value = "채팅하기", notes = "판매자와 구매자의 채팅을 진행합니다.")
    @ApiImplicitParam(name = "boardId", value = "게시글 고유 id", example = "1", required = true, dataTypeClass = Long.class, paramType = "path")
    @GetMapping("/chat/{boardId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<ChatRoomResponse> startChatting(
            @PathVariable Long boardId,
            @ApiIgnore UserPrincipal userPrincipal) {

        String buyerEmail = userPrincipal.getEmail();

        ChattingRoom chatRoom = chatService.findChatRoom(boardId, buyerEmail).orElse(
                ChattingRoom.builder()
                        .board(boardService.findById(boardId))
                        .buyer(userService.findByEmail(buyerEmail))
                        .build()
        );

        chatService.createChatRoom(chatRoom);

        List<ChattingContent> contentList = chatService.findChatContentByChatRoomId(chatRoom.getId());
        chatRoom.updateChatContent(contentList);

        return succeed(new ChatRoomResponse(chatRoom));
    }

    @ApiOperation(value = "메시지 보내기", notes = "채팅 메시지를 전송합니다.")
    @MessageMapping("/broadcast")
    public void sendChat(ChatContentRequest request) {
        chatService.createChatContent(request);
        Long chatRoomId = request.getChatRoomId();
        String url = "/user/" + chatRoomId + "/queue/messages";
        simpMessagingTemplate.convertAndSend(
                url,
                new ChatRoomResponse(
                        chatService.findChatRoom(chatRoomId)
                )
        );
    }
}
