package io.wisoft.vamos.domain.admin;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "admin")
@Getter
public class Admin extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "password")
    private String password;
}
