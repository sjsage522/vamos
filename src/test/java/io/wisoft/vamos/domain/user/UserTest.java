package io.wisoft.vamos.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("도메인 테스트 (User)")
class UserTest {

    @Test
    @DisplayName("사용자 생성 테스트")
    void create_user_test() {

        PhoneNumber phoneNumber = PhoneNumber.of("01012345678");

        User user = User.from("testId", "1234", phoneNumber, "tester");
        settingUser(user);

        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals("testId", user.getUsername()),
                () -> assertEquals("1234", user.getPassword()),
                () -> assertEquals("01012345678", phoneNumber.getPhoneNumber()),
                () -> assertEquals("tester", user.getNickname()),
                () -> assertTrue(user.getAuthorities().contains(Authority.of("ROLE_USER"))));
    }

    private void settingUser(User user) {
        user.setAuthority(Collections.singleton(Authority.of("ROLE_USER")));
        user.setEncodedPassword(user.getPassword());
    }
}