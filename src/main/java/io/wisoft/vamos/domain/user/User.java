package io.wisoft.vamos.domain.user;

import com.google.common.base.Preconditions;
import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private UserLocation location;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities = new HashSet<>();

    protected User() { /* empty */ }

    private User(String username, String password, PhoneNumber phoneNumber, String nickname, UserLocation location) {
        Preconditions.checkArgument(checkUserId(username), "아이디는 영 소대문자, 숫자 5~20 자리로 입력해주세요.");
        //TODO 필드 체크
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.location = location;
    }

    public static class Builder {
        private String username;
        private String password;
        private PhoneNumber phoneNumber;
        private String nickname;
        private UserLocation location;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder phoneNumber(PhoneNumber phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder userLocation(UserLocation location) {
            this.location = location;
            return this;
        }

        public User build() {
            return new User(username, password, phoneNumber, nickname, location);
        }
    }

    public static User from(String username, String password, PhoneNumber phoneNumber, String nickName, UserLocation location) {
        return new User(username, password, phoneNumber, nickName, location);
    }

    public void changeUserLocation(UserLocation location) {
        this.location = location;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void setAuthority(Set<Authority> authorities) {
        this.authorities = authorities;
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

    private boolean checkUserId(String username) {
        return Pattern.matches("^[a-zA-Z][a-zA-Z0-9]{4,19}$", username);
    }
}
