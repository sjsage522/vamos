package io.wisoft.vamos.service;

import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import io.wisoft.vamos.dto.user.UserLocationUpdateRequest;
import io.wisoft.vamos.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("사용자 서비스 테스트")
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("사용자위치 수정 성공 테스트")
    void updateUserLocation_succeed_test() {
        //given
        String email = "junseok@gmail.com";
        User user = User.builder()
                .email(email)
                .location(UserLocation.from(0., 0., "test location"))
                .build();
        UserLocationUpdateRequest request = UserLocationUpdateRequest.builder()
                .x(1.)
                .y(1.)
                .addressName("update location")
                .build();

        //when
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));
        userService.updateUserLocation(email, request);

        //then
        UserLocation location = user.getLocation();
        assertThat(1.).isEqualTo(location.getX());
        assertThat(1.).isEqualTo(location.getY());
        assertThat("update location").isEqualTo(location.getAddressName());
    }
}

