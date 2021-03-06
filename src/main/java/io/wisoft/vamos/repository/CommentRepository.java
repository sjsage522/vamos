package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.comment.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c " +
            "where c.parent is null " +
            "and c.board.id = :boardId " +
            "order by c.id asc")
    @EntityGraph(attributePaths = {"user"})
    List<Comment> findAllByBoardIdIfParentIsNull(Long boardId);

    @Query("select c from Comment c where c.id = :commentId")
    @EntityGraph(attributePaths = {"user"})
    Optional<Comment> findByIdWithUser(Long commentId);

    List<Comment> findAllByBoardId(Long boardId);

    @Modifying
    @Query("delete from Comment c where c.id in :ids")
    void deleteWithIds(List<Long> ids);
}
