package io.wisoft.vamos.domain.comment;

import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("도메인 테스트 (Comment)")
class CommentTest {

    @Test
    @DisplayName("답글 생성 성공 테스트")
    void create_comment_succeed_test() {
        User user = mock(User.class);
        Board board = mock(Board.class);

        Comment comment = Comment.from(user, board, "content");

        assertThat(comment.getUser()).isNotNull();
        assertThat(comment.getBoard()).isNotNull();
        assertThat(comment.getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("답글 생성 실패 테스트")
    void create_comment_failed_test() {
        User user = mock(User.class);
        Board board = mock(Board.class);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Comment.from(null, board, "content")),
                () -> assertThrows(IllegalArgumentException.class, () -> Comment.from(user, null, "content")),
                () -> assertThrows(IllegalArgumentException.class, () -> Comment.from(user, board, "")),
                () -> assertThrows(IllegalArgumentException.class, () -> Comment.from(user, board, null))
        );
    }
}