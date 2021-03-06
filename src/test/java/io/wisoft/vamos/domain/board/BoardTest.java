package io.wisoft.vamos.domain.board;

import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.category.CategoryName;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("도메인 테스트 (Board)")
class BoardTest {

    private static User user;

    @BeforeAll
    static void userInit() {
        user = User.builder()
                .username("testId")
                .email("junseok@gmail.com")
                .phoneNumber(PhoneNumber.of("01012345678"))
                .nickname("tester")
                .location(UserLocation.from(0., 0., "test location"))
                .build();
    }

    @Test
    @DisplayName("게시글 생성 성공 테스트")
    void create_board_test() {
        Board board = Board.from("test title", "test content", 1,
                user,
                Category.of("BOOKS_TICKETS_RECORDS"));


        assertThat(board.getTitle()).isEqualTo("test title");
        assertThat(board.getUser().getUsername()).isEqualTo("testId");
        assertThat(board.getCategory().getName()).isEqualTo(CategoryName.BOOKS_TICKETS_RECORDS);
    }

    @Test
    @DisplayName("게시글 생성 실패 테스트")
    void create_board_failed_test() {

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Board.from("test title", "test content", 1,
                        null,
                        Category.of("DIGITAL_DEVICE")), "사용자는 반드시 존재해야 합니다."),

                () -> assertThrows(IllegalArgumentException.class, () -> Board.from("test title", "test content", 1,
                        user,
                        null), "카테고리는 반드시 존재해야 합니다."),

                () -> assertThrows(IllegalArgumentException.class, () -> Board.from("", "test conetent", 1,
                        user,
                        Category.of("DIGITAL_DEVICE")), "title 은 반드시 존재해야 합니다."),

                () -> assertThrows(IllegalArgumentException.class, () -> Board.from("test title", null, 1,
                        user,
                        Category.of("DIGITAL_DEVICE")), "content 은 반드시 존재해야 합니다."),

                () -> assertThrows(IllegalArgumentException.class, () -> Board.from("test title", "test content", 0,
                        user,
                        Category.of("DIGITAL_DEVICE")), "가격은 0 보다 커야합니다.")
        );
    }
}