package io.wisoft.vamos.service;

import io.wisoft.vamos.common.exception.DataAlreadyExistsException;
import io.wisoft.vamos.domain.user.Authority;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("사용자 회원가입 성공 테스트")
    void _01_user_join_success_test() {

        //given
        PhoneNumber phoneNumber = PhoneNumber.of("01012345678");
        User user = User.from("testId", "1234", phoneNumber, "tester");

        given(userRepository.save(user))
                .willReturn(user);
        given(userRepository.findDuplicateUserCount("testId", phoneNumber, "tester"))
                .willReturn(0);

        //when
        UserService userService = new UserService(userRepository, passwordEncoder);

        userService.join(user);

        //then
        assertThat(user.getAuthorities().contains(Authority.of("ROLE_USER"))).isTrue();
    }

    @Test
    @DisplayName("사용자 회원가입 실패 테스트")
    void _02_user_join_failed_test() {

        //given
        PhoneNumber phoneNumber = PhoneNumber.of("01012345678");
        User user = User.from("testId", "1234", phoneNumber, "tester");

        given(userRepository.findDuplicateUserCount("testId", phoneNumber, "tester"))
                .willReturn(1);

        //when
        UserService userService = new UserService(userRepository, passwordEncoder);

        //then
        assertThrows(DataAlreadyExistsException.class, () -> userService.join(user));
    }
}