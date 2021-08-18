package io.wisoft.vamos.service;

import io.wisoft.vamos.exception.DataAlreadyExistsException;
import io.wisoft.vamos.domain.user.Authority;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.user.UserJoinRequest;
import io.wisoft.vamos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("사용자 서비스 테스트")
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("사용자 회원가입 성공 테스트")
    void _01_user_join_success_test() {

        //given
        UserJoinRequest userJoinRequest = getUserJoinRequest();
        User user = getUser(userJoinRequest);

        given(userRepository.save(user))
                .willReturn(user);
        given(userRepository.findDuplicateUserCount("testId", PhoneNumber.of("01012345678"), "tester"))
                .willReturn(0);

        //when
        User saveUser = userService.join(userJoinRequest);

        //then
        assertThat(saveUser.getAuthorities().contains(Authority.of("ROLE_USER"))).isTrue();
    }

    @Test
    @DisplayName("사용자 회원가입 실패 테스트 (사용자 닉네임 중복)")
    void _02_user_join_failed_test() {

        //given
        UserJoinRequest userJoinRequest = getUserJoinRequest();


        given(userRepository.findDuplicateUserCount("testId", PhoneNumber.of("01012345678"), "tester"))
                .willReturn(1);

        //when

        //then
        assertThrows(DataAlreadyExistsException.class, () -> userService.join(userJoinRequest));
    }

    @Test
    @DisplayName("사용자 회원가입 실패 테스트 (사용자 아이디 중복)")
    void _03_user_join_failed_test() {

        //given
        UserJoinRequest userJoinRequest = getUserJoinRequest();

        given(userRepository.findDuplicateUserCount("testId", PhoneNumber.of("01012345678"), "tester"))
                .willReturn(1);

        //when

        //then
        assertThrows(DataAlreadyExistsException.class, () -> userService.join(userJoinRequest));
    }

    @Test
    @DisplayName("사용자 회원가입 실패 테스트 (사용자 전화번호 중복)")
    void _04_user_join_failed_test() {

        //given
        UserJoinRequest userJoinRequest = getUserJoinRequest();

        given(userRepository.findDuplicateUserCount("testId", PhoneNumber.of("01012345678"), "tester"))
                .willReturn(1);

        //when

        //then
        assertThrows(DataAlreadyExistsException.class, () -> userService.join(userJoinRequest));
    }

    private User getUser(UserJoinRequest request) {

        UserLocation location = UserLocation.from(request.getX(), request.getY(), request.getAddressName());

        final User user = User.from(
                request.getUsername(),
                request.getPassword(),
                PhoneNumber.of(request.getPhoneNumber()),
                request.getNickname(),
                location
        );
        settingUser(user);

        return user;
    }

    private void settingUser(User user) {
        user.setAuthority(Collections.singleton(Authority.of("ROLE_USER")));
        user.setEncodedPassword(passwordEncoder.encode(user.getEmail()));
    }

    private UserJoinRequest getUserJoinRequest() {
        UserJoinRequest userJoinRequest = new UserJoinRequest();
        userJoinRequest.setUsername("testId");
        userJoinRequest.setPassword("1234");
        userJoinRequest.setNickname("tester");
        userJoinRequest.setPhoneNumber("01012345678");
        userJoinRequest.setX(0.0);
        userJoinRequest.setY(0.0);
        userJoinRequest.setAddressName("test location");
        return userJoinRequest;
    }
}

