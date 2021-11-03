package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.chatting.ChattingRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChattingRoom, Long> {

    Optional<ChattingRoom> findByBoardIdAndBuyer_Email(Long boardId, @Param("email") String buyerEmail);

    @Query("SELECT cr FROM ChattingRoom cr WHERE cr.id = :chatRoomId")
    @EntityGraph(attributePaths = {"chattingContent"})
    Optional<ChattingRoom> findByIdWithContents(Long chatRoomId);
}
