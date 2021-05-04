package io.wisoft.vamos.domain.board;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.location.Dong;
import io.wisoft.vamos.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "board")
@Getter
public class Board extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_id")
    private Dong dong;

    @Column(name = "board_status")
    @Enumerated(EnumType.STRING)
    private BoardStatus status;
}
