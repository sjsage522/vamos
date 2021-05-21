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

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "password")
    private String password;

    @Embedded
    private PhoneNumber phoneNumber;

    @Column(name = "nick_name", unique = true)
    private String nickName;

    protected User() { /* empty */ }

    private User(String userId, String password, PhoneNumber phoneNumber, String nickName) {
        this.userId = userId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
    }

    public static User from(String userId, String password, PhoneNumber phoneNumber, String nickName) {
        return new User(userId, password, phoneNumber, nickName);
    }

    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
