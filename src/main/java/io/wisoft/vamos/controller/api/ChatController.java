package io.wisoft.vamos.controller.api;

import io.wisoft.vamos.config.auth.LoginUser;
import io.wisoft.vamos.config.auth.dto.SessionUser;
import io.wisoft.vamos.domain.board.BoardInfoCriteria;
import io.wisoft.vamos.domain.chatting.ChattingContent;
import io.wisoft.vamos.domain.chatting.ChattingRoom;
import io.wisoft.vamos.dto.api.ApiResult;
import io.wisoft.vamos.dto.chat.ChatContentRequest;
import io.wisoft.vamos.dto.chat.ChatRoomResponse;
import io.wisoft.vamos.service.BoardService;
import io.wisoft.vamos.service.ChatService;
import io.wisoft.vamos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.wisoft.vamos.dto.api.ApiResult.succeed;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final BoardService boardService;
    private final UserService userService;

    /**
     * 채팅으로 거래하기
     *
     * @param boardInfoCriteria 게시글 정보 폼데이터
     * @param sessionUser       현재 로그인 사용자
     * @return 채팅방 정보
     */
    @GetMapping("/chat")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<ChatRoomResponse> startChatting(
            BoardInfoCriteria boardInfoCriteria,
            @LoginUser SessionUser sessionUser) {

        Long boardId = boardInfoCriteria.getId();
        String buyerEmail = sessionUser.getEmail();

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
