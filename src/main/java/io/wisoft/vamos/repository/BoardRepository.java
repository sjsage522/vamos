package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.board.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(
            value = "SELECT * FROM board b INNER JOIN users u ON b.user_id = u.id AND earth_distance(" +
                    "ll_to_earth(CAST(b.y as float8), CAST(b.x as float8))," +
                    "ll_to_earth(:latitude, :longitude)) < :radius",
            nativeQuery = true
    )
    List<Board> findByEarthDistance(@Param("longitude") Double x, @Param("latitude") Double y, @Param("radius") int radius);

    @Query(
            value = "SELECT b FROM Board b WHERE b.id = :boardId"
    )
    @EntityGraph(attributePaths = {"chattingRooms"})
    Optional<Board> findByBoardIdWithChatRoom(Long boardId);

    List<Board> findAllByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Board b WHERE b.id IN :ids")
    void deleteWithIds(List<Long> ids);
}
