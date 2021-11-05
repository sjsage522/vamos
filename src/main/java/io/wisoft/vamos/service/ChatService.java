package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.chatting.ChattingContent;
import io.wisoft.vamos.domain.chatting.ChattingRoom;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.dto.chat.ChatContentRequest;
import io.wisoft.vamos.exception.NoMatchChattingRoomException;
import io.wisoft.vamos.exception.NoMatchUserInfoException;
import io.wisoft.vamos.repository.BoardRepository;
import io.wisoft.vamos.repository.ChatContentRepository;
import io.wisoft.vamos.repository.ChatRoomRepository;
import io.wisoft.vamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatContentRepository chatContentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<ChattingRoom> findChatRoom(Long boardId, Long buyerId) {
        return chatRoomRepository.findByBoardIdAndBuyerId(boardId, buyerId);
    }

    @Transactional(readOnly = true)
    public ChattingRoom findChatRoom(Long chatRoomId) {
        return chatRoomRepository.findByIdWithContents(chatRoomId).orElseThrow(NoMatchChattingRoomException::new);
    }

    @Transactional
    public ChattingRoom createChatRoom(ChattingRoom initChatRoom) {
        return chatRoomRepository.save(initChatRoom);
    }

    @Transactional
    public void deleteChatRoomsByBoardId(Long boardId) {
        List<ChattingRoom> chattingRooms = chatRoomRepository.findChattingRoomWithContentsByBoardId(boardId);

        List<Long> roomIds = new ArrayList<>();
        for (ChattingRoom chattingRoom : chattingRooms) {
            roomIds.add(chattingRoom.getId());
            List<Long> contentIds = chattingRoom.getChattingContents().stream()
                    .map(ChattingContent::getId)
                    .collect(Collectors.toList());
            chatContentRepository.deleteWithIds(contentIds);
        }

        chatRoomRepository.deleteWithIds(roomIds);
    }

    @Transactional(readOnly = true)
    public List<ChattingContent> findChatContentByChatRoomId(Long chatRoomId) {
        return chatContentRepository.findAllByChattingRoomId(chatRoomId);
    }

    @Transactional(readOnly = true)
    public List<ChattingRoom> findChatRoomBySellerId(Long sellerId) {
        return chatRoomRepository.findChattingRoomBySellerId(sellerId);
    }

    @Transactional(readOnly = true)
    public List<ChattingRoom> findChatRoomByBuyerId(Long sellerId) {
        return chatRoomRepository.findChattingRoomByBuyerId(sellerId);
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
