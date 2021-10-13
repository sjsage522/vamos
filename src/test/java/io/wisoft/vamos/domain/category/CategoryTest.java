package io.wisoft.vamos.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("도메인 테스트 (Category)")
public class CategoryTest {

    @Test
    @DisplayName("카테고리 생성 성공 테스트")
    void create_category_succeed_test() {
        final Category category1 = Category.of(1L);
        assertThat(category1.getName().getEn()).isEqualTo("DIGITAL_DEVICE");

        final Category category2 = Category.of("DIGITAL_DEVICE");
        assertThat(category2.getName().getKr()).isEqualTo("디지털기기");
    }

    @Test
    @DisplayName("카테고리 생성 실패 테스트")
    void create_category_failed_test() {
        Assertions.assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Category.of(100L), "존재하지 않는 카테고리 입니다.")
        );
    }
}
