package io.wisoft.vamos.domain.admin;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "notice")
@Getter
@SequenceGenerator(
        name = "notice_sequence_generator",
        sequenceName = "notice_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class Notice extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notice_sequence_generator")
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
}
