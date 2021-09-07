package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.chatting.ChattingContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatContentRepository extends JpaRepository<ChattingContent, Long> {

    List<ChattingContent> findAllByChattingRoomId(Long chattingRoomId);
}
