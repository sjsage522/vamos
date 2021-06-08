package io.wisoft.vamos.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("도메인 테스트 (PhoneNumber)")
class PhoneNumberTest {

    @Test
    @DisplayName("전화번호 생성 테스트")
    void create_phoneNumber_test() {

        PhoneNumber valid = PhoneNumber.of("01012345678");
        assertThat(valid.getPhoneNumber()).isEqualTo("01012345678");

        assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of("010-1234-5678"));
        assertThrows(IllegalArgumentException.class, () -> PhoneNumber.of("0102345"));
    }
}