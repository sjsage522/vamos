package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.chatting.ChattingContent;
import io.wisoft.vamos.domain.chatting.ChattingRoom;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.dto.chat.ChatContentRequest;
import io.wisoft.vamos.exception.NoMatchChattingRoomException;
import io.wisoft.vamos.exception.NoMatchUserInfoException;
import io.wisoft.vamos.repository.ChatContentRepository;
import io.wisoft.vamos.repository.ChatRoomRepository;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatContentRepository chatContentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<ChattingRoom> findChatRoom(Long boardId, String buyerEmail) {
        return chatRoomRepository.findByBoardIdAndBuyer_Email(boardId, buyerEmail);
    }

    @Transactional(readOnly = true)
    public ChattingRoom findChatRoom(Long chatRoomId) {
        return chatRoomRepository.findByIdWithContents(chatRoomId).orElseThrow(NoMatchChattingRoomException::new);
    }

    @Transactional
    public ChattingRoom createChatRoom(ChattingRoom initChatRoom) {
        return chatRoomRepository.save(initChatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChattingContent> findChatContentByChatRoomId(Long chatRoomId) {
        return chatContentRepository.findAllByChattingRoomId(chatRoomId);
    }

    @Transactional
    public ChattingContent createChatContent(ChatContentRequest request) {
        User writer = userRepository.findByEmail(request.getWriterEmail()).orElseThrow(NoMatchUserInfoException::new);
        ChattingRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId()).orElseThrow(NoMatchChattingRoomException::new);
        String message = request.getContent();

        ChattingContent initChatContent = ChattingContent.builder()
                .writer(writer)
                .chattingRoom(chatRoom)
                .content(message)
                .build();

        return chatContentRepository.save(initChatContent);
    }
}
