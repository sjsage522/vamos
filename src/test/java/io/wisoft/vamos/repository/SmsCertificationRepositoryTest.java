package io.wisoft.vamos.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("SMS 인증 레포지토리 테스트")
class SmsCertificationRepositoryTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    SmsCertificationRepository smsCertificationRepository;

    @BeforeAll
    void init() {
        smsCertificationRepository = new SmsCertificationRepository(stringRedisTemplate);
    }

    @Test
    @DisplayName("01. 인증 번호 저장 및 추출 테스트")
    void _01_certification_create_and_get_test() {

        smsCertificationRepository.createSmsCertification("01012345678", "123456");
        String certification = smsCertificationRepository.getSmsCertification("01012345678");

        assertThat(certification).isEqualTo("123456");
    }

    @Test
    @DisplayName("02. 인증 번호 포함확인 테스트")
    void _02_certification_hasKey_test() {

        assertThat(smsCertificationRepository.hasKey("01012345678")).isTrue();
    }

    @Test
    @DisplayName("03. 인증 번호 삭제 테스트")
    void _03_certification_remove_test() {

        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> smsCertificationRepository.removeSmsCertification("123")),
                () -> smsCertificationRepository.removeSmsCertification("01012345678")
        );
    }
}