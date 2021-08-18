package io.wisoft.vamos.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("도메인 테스트 (User)")
class UserTest {

    @Test
    @DisplayName("사용자 생성 성공 테스트")
    void create_user_test() {

        PhoneNumber phoneNumber = PhoneNumber.of("01012345678");
        UserLocation userLocation = UserLocation.from(0.0, 0.0, "test location");

        User user = getUser(phoneNumber, userLocation, "testId", "1234", "tester");

        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals("testId", user.getUsername()),
                () -> assertEquals("1234", user.getEmail()),
                () -> assertEquals("01012345678", phoneNumber.getPhoneNumber()),
                () -> assertEquals("tester", user.getNickname()),
                () -> assertTrue(user.getAuthorities().contains(Authority.of("ROLE_USER"))));
    }

    @Test
    @DisplayName("사용자 생성 실패 테스트")
    void create_user_failed_test() {

        PhoneNumber phoneNumber = PhoneNumber.of("01012345678");
        UserLocation userLocation = UserLocation.from(0.0, 0.0, "test location");

        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> getUser(phoneNumber, userLocation, "1234", "1234", "tester"),
                        "아이디는 숫자로 시작할 수 없습니다."),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> getUser(phoneNumber, userLocation, "abc1", "1234", "tester"),
                        "아이디는 5자리이상 20자리 이하여야 합니다."),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> getUser(phoneNumber, userLocation, "_testId", "1234", "tester"),
                        "특수문자를 사용할 수 없습니다."),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> getUser(phoneNumber, userLocation, "testIdAAAAAAAAAAAAAAE", "1234", "tester"),
                        "아이디는 20자리 이하여야 합니다.")
        );
    }

    @Test
    @DisplayName("사용자 비교 테스트")
    void user_equals_test() {
        PhoneNumber phoneNumber = PhoneNumber.of("01012345678");
        UserLocation userLocation = UserLocation.from(0.0, 0.0, "test location");

        User user1 = getUser(phoneNumber, userLocation, "testId", "1234", "tester");
        User user2 = getUser(phoneNumber, userLocation, "testId", "1234", "tester");

        Assertions.assertThat(user1.equals(user2)).isTrue();
    }

    private User getUser(PhoneNumber phoneNumber, UserLocation userLocation, String username, String password, String nickName) {
        User user = User.from(username, password, phoneNumber, nickName, userLocation);
        settingUser(user);
        return user;
    }

    private void settingUser(User user) {
        user.setAuthority(Collections.singleton(Authority.of("ROLE_USER")));
        user.setEncodedPassword(user.getEmail());
    }
}