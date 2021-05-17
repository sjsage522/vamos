package io.wisoft.vamos.domain.admin;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "admin")
@Getter
@SequenceGenerator(
        name = "admin_sequence_generator",
        sequenceName = "admin_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class Admin extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "admin_sequence_generator")
    private Long id;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "password")
    private String password;
}
