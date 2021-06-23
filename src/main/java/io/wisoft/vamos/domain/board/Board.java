package io.wisoft.vamos.domain.board;

import com.google.common.base.Preconditions;
import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

import java.util.Arrays;
import java.util.Objects;

import static com.google.common.base.Preconditions.*;

@Entity
@Table(name = "board")
@Getter
@SequenceGenerator(
        name = "board_sequence_generator",
        sequenceName = "board_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class Board extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "board_sequence_generator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "board_status")
    @Enumerated(EnumType.STRING)
    private BoardStatus status;     /* default -> SALE */

    protected Board() {}

    private Board(String title, String content, int price, User user, Category category) {
        checkArgument(checkStringValid(title, content), "제목이나 내용은 비워둘 수 없습니다.");
        checkArgument(checkPriceValid(price), "가격은 0 보다 커야합니다.");
        checkArgument(checkObjectValid(user, category), "사용자 또는 카테고리가 유효하지 않습니다.");
        this.title = title;
        this.content = content;
        this.price = price;
        this.user = user;
        this.category = category;
    }

    public static Board from(String title, String content, int price, User user, Category category) {
        return new Board(title, content, price, user, category);
    }

    public void changeStatus(BoardStatus status) {
        this.status = status;
    }

    private boolean checkStringValid(String... values) {
        return Arrays.stream(values)
                .noneMatch(value -> value == null || value.isBlank());
    }

    private boolean checkPriceValid(int price) {
        return price > 0;
    }

    private boolean checkObjectValid(Object... objects) {
        return Arrays.stream(objects)
                .noneMatch(Objects::isNull);
    }
}
