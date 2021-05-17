package io.wisoft.vamos.domain.user;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "following_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "target_id"})
        })
@Getter
@SequenceGenerator(
        name = "following_user_sequence_generator",
        sequenceName = "following_user_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class FollowingUser extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "following_user_sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private User target;
}
