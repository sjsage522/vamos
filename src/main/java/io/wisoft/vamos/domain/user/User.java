package io.wisoft.vamos.domain.user;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@SequenceGenerator(
        name = "user_sequence_generator",
        sequenceName = "user_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence_generator")
    private Long id;

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(name = "nick_name", unique = true)
    private String nickName;

    protected User() { /* empty */ }

    private User(PhoneNumber phoneNumber, String nickName) {
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
    }

    public static User from(PhoneNumber phoneNumber, String nickName) {
        return new User(phoneNumber, nickName);
    }
}
