package io.wisoft.vamos.domain.board;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

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
}
