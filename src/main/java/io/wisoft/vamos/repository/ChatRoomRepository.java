package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.chatting.ChattingRoom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChattingRoom, Long> {

    Optional<ChattingRoom> findByBoardIdAndBuyerId(Long boardId, Long buyerId);

    @Query("SELECT cr FROM ChattingRoom cr WHERE cr.id = :chatRoomId")
    @EntityGraph(attributePaths = {"chattingContents"})
    Optional<ChattingRoom> findByIdWithContents(Long chatRoomId);

    @EntityGraph(attributePaths = {"board"})
    List<ChattingRoom> findChattingRoomBySellerId(Long id);

    @EntityGraph(attributePaths = {"board"})
    List<ChattingRoom> findChattingRoomByBuyerId(Long id);

    @EntityGraph(attributePaths = {"chattingContents"})
    List<ChattingRoom> findChattingRoomWithContentsByBoardId(Long id);

    @Modifying
    @Query("delete from ChattingRoom cr where cr.id in :ids")
    void deleteWithIds(List<Long> ids);
}
