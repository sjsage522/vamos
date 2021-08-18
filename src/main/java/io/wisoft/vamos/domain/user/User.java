package io.wisoft.vamos.domain.user;

import com.google.common.base.Preconditions;
import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;
import java.util.regex.Pattern;

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

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "picture")
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private UserLocation location;

    protected User() { /* empty */ }

    @Builder
    private User(String email, String username, String nickname, String picture, Role role, PhoneNumber phoneNumber, UserLocation location) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.picture = picture;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public void changeUserLocation(UserLocation location) {
        this.location = location;
    }

    public User update(String username, String picture) {
        this.username = username;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getNickname(), user.getNickname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getNickname());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", picture='" + picture + '\'' +
                ", role=" + role +
                ", phoneNumber=" + phoneNumber +
                ", location=" + location +
                '}';
    }

    private boolean checkUserId(String username) {
        return Pattern.matches("^[a-zA-Z][a-zA-Z0-9]{4,19}$", username);
    }
}
