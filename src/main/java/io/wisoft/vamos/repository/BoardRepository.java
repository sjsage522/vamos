package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(
            value = "SELECT * FROM board b INNER JOIN users u ON b.user_id = u.id AND earth_distance(" +
                    "ll_to_earth(CAST(u.y as float8), CAST(u.x as float8))," +
                    "ll_to_earth(:latitude, :longitude)) < :radius",
            nativeQuery = true
    )
    List<Board> findByEarthDistance(@Param("longitude") Double x, @Param("latitude") Double y, int radius);
}
