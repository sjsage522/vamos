package io.wisoft.vamos.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("도메인 테스트 (Authority)")
class AuthorityTest {

    @Test
    @DisplayName("권한 생성 테스트")
    void create_authority_test() {

        Authority user_role = Authority.of("ROLE_USER");
        assertThat(user_role.getAuthorityName()).isEqualTo("ROLE_USER");

        Authority user_admin = Authority.of("ROLE_ADMIN");
        assertThat(user_admin.getAuthorityName()).isEqualTo("ROLE_ADMIN");

        assertThrows(IllegalArgumentException.class, () -> Authority.of("ROLE_GUEST"));
    }
}