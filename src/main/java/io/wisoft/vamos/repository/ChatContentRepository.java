package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.chatting.ChattingContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatContentRepository extends JpaRepository<ChattingContent, Long> {

    List<ChattingContent> findAllByChattingRoomId(Long chattingRoomId);

    @Modifying
    @Query("delete from ChattingContent c where c.id in :ids")
    void deleteWithIds(List<Long> ids);
}
