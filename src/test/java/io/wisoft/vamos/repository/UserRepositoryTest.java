package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.user.Authority;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("사용자 레포지토리 테스트")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    static User user;

    @BeforeAll
    static void init() {
        user = User.from(
                "testId",
                "1234",
                PhoneNumber.of("01012345678"),
                "tester",
                UserLocation.from(0.0, 0.0, "test location")
        );
        settingUser(user);
    }

    @BeforeEach
    void before() {
        userRepository.save(user);
        em.flush();
        em.clear();
    }

    private static void settingUser(User user) {
        user.setAuthority(Collections.singleton(Authority.of("ROLE_USER")));
        user.setEncodedPassword(user.getPassword());
    }

    @Test
    @DisplayName("사용자 조회 테스트 (nickname)")
    void findByNickName_test() {

        //given
        //when
        User findUser = userRepository.findByNickname("tester").get();

        //then
        assertThat(findUser.getNickname()).isEqualTo("tester");
    }

    @Test
    @DisplayName("사용자 조회 테스트 (phoneNumber)")
    void findByPhoneNumber_test() {

        //given
        //when
        User findUser = userRepository.findByPhoneNumber(PhoneNumber.of("01012345678")).get();

        //then
        assertThat(findUser.getPhoneNumber()).isEqualTo(PhoneNumber.of("01012345678"));
    }

    @Test
    @DisplayName("사용자 조회 테스트 (username)")
    void findByUsername_test() {

        //given
        //when
        User findUser = userRepository.findByUsername("testId").get();

        //then
        assertThat(findUser.getUsername()).isEqualTo("testId");
    }

    @Test
    @DisplayName("사용자 중복검사 조회 테스트")
    void duplicate_user_test() {

        assertAll(
                () -> assertEquals(1, userRepository.findDuplicateUserCount(
                        "testId",                /* unique constraint */
                        PhoneNumber.of("01011112222"),
                        "person")),
                () -> assertEquals(1, userRepository.findDuplicateUserCount(
                        "testId2",
                        PhoneNumber.of("01012345678"),    /* unique constraint */
                        "person")),
                () -> assertEquals(1, userRepository.findDuplicateUserCount(
                        "testId3",
                        PhoneNumber.of("01022223333"),
                        "tester"                 /* unique constraint */
                )))
        ;
    }
}