package io.wisoft.vamos.domain.user;

import io.wisoft.vamos.domain.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "nick_name")
    private String nickName;
}
