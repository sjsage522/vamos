package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.user.Role;
import io.wisoft.vamos.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("사용자 레포지토리 테스트")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("이메일로 사용자 찾기 성공 테스트")
    void user_findByEmail_succeed_test() {
        //given
        String email = "junseok@gmail.com";

        User user = User.builder()
                .email(email)
                .role(Role.GUEST)
                .build();
        userRepository.save(user);

        //when
        User findUser = userRepository.findByEmail(email).get();

        //then
        Assertions.assertThat(email).isEqualTo(findUser.getEmail());
    }
}
