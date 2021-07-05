package io.wisoft.vamos.service;

import io.wisoft.vamos.common.exception.DataAlreadyExistsException;
import io.wisoft.vamos.domain.user.Authority;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("사용자 서비스 테스트")
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    UserService userService;

    @BeforeEach
    void serviceInit() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    @BeforeEach
    void userCreate() {

    }

    @Test
    @DisplayName("사용자 회원가입 성공 테스트")
    void _01_user_join_success_test() {

        //given

        PhoneNumber phoneNumber = PhoneNumber.of("01012345678");
        UserLocation userLocation = UserLocation.from(0.0, 0.0, "test location");

        User user = User.from("testId", "1234", phoneNumber, "tester", userLocation);

        given(userRepository.save(user))
                .willReturn(user);
        given(userRepository.findDuplicateUserCount("testId", phoneNumber, "tester"))
                .willReturn(0);

        //when
        userService.join(user);

        //then
        assertThat(user.getAuthorities().contains(Authority.of("ROLE_USER"))).isTrue();
    }

    @Test
    @DisplayName("사용자 회원가입 실패 테스트 (사용자 닉네임 중복)")
    void _02_user_join_failed_test() {

        //given
        PhoneNumber phoneNumber = PhoneNumber.of("01023456789");
        UserLocation userLocation = UserLocation.from(0.0, 0.0, "test location");

        User user = User.from("testId2", "1234", phoneNumber, "tester", userLocation);

        given(userRepository.findDuplicateUserCount("testId2", phoneNumber, "tester"))
                .willReturn(1);

        //when

        //then
        assertThrows(DataAlreadyExistsException.class, () -> userService.join(user));
    }

    @Test
    @DisplayName("사용자 회원가입 실패 테스트 (사용자 아이디 중복)")
    void _03_user_join_failed_test() {

        //given
        PhoneNumber phoneNumber = PhoneNumber.of("01023456789");
        UserLocation userLocation = UserLocation.from(0.0, 0.0, "test location");

        User user = User.from("testId", "1234", phoneNumber, "tester2", userLocation);

        given(userRepository.findDuplicateUserCount("testId", phoneNumber, "tester2"))
                .willReturn(1);

        //when

        //then
        assertThrows(DataAlreadyExistsException.class, () -> userService.join(user));
    }

    @Test
    @DisplayName("사용자 회원가입 실패 테스트 (사용자 전화번호 중복)")
    void _04_user_join_failed_test() {

        //given
        PhoneNumber phoneNumber = PhoneNumber.of("01023456789");
        UserLocation userLocation = UserLocation.from(0.0, 0.0, "test location");

        User user = User.from("testId", "1234", phoneNumber, "tester", userLocation);

        given(userRepository.findDuplicateUserCount("testId", phoneNumber, "tester"))
                .willReturn(1);

        //when

        //then
        assertThrows(DataAlreadyExistsException.class, () -> userService.join(user));
    }
}