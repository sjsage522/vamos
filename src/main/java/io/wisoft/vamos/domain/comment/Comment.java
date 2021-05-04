package io.wisoft.vamos.domain.comment;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @Column(name = "comment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
