package io.wisoft.vamos.domain.chatting;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.board.Board;
import io.wisoft.vamos.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "chatting_room",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "board_id"})
        })
@Getter
@SequenceGenerator(
        name = "chatting_room_sequence_generator",
        sequenceName = "chatting_room_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class ChattingRoom extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chatting_room_sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "chatting_category")
    @Enumerated(EnumType.STRING)
    private ChattingCategory category;
}
