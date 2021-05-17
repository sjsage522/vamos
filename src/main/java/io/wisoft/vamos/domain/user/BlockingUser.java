package io.wisoft.vamos.domain.user;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "blocking_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "target_id"})
        })
@Getter
@SequenceGenerator(
        name = "blocking_user_sequence_generator",
        sequenceName = "blocking_user_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class BlockingUser extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "blocking_user_sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private User target;
}
