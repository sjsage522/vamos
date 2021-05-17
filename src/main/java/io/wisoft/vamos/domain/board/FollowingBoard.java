package io.wisoft.vamos.domain.board;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "following_board",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "board_id"})
        })
@Getter
@SequenceGenerator(
        name = "following_board_sequence_generator",
        sequenceName = "following_board_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class FollowingBoard extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "following_board_sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
