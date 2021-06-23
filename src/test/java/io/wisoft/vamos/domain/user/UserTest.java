package io.wisoft.vamos.domain.user;

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

        User user = User.from("testId", "1234", phoneNumber, "tester", userLocation);
        settingUser(user);

        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals("testId", user.getUsername()),
                () -> assertEquals("1234", user.getPassword()),
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
                        () -> User.from("1234", "1234", phoneNumber, "tester", userLocation),
                        "아이디는 숫자로 시작할 수 없습니다."),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> User.from("abc1", "1234", phoneNumber, "tester", userLocation),
                        "아이디는 5자리이상 20자리 이하여야 합니다."),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> User.from("_testId", "1234", phoneNumber, "tester", userLocation),
                        "특수문자를 사용할 수 없습니다."),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> User.from("testIdAAAAAAAAAAAAAAE", "1234", phoneNumber, "tester", userLocation),
                        "아이디는 20자리 이하여야 합니다.")
        );
    }

    private void settingUser(User user) {
        user.setAuthority(Collections.singleton(Authority.of("ROLE_USER")));
        user.setEncodedPassword(user.getPassword());
    }
}