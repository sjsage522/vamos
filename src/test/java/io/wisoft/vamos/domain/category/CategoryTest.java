package io.wisoft.vamos.domain.category;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("도메인 테스트 (Category)")
class CategoryTest {

    @Test
    @DisplayName("카테고리 생성 성공 테스트")
    void create_category_test() {

        Category category = Category.of("DIGITAL_DEVICE");
        Assertions.assertThat(category.getName()).isEqualTo(CategoryName.DIGITAL_DEVICE);
    }

    @Test
    @DisplayName("카테고리 생성 실패 테스트")
    void create_category_failed_test() {

        assertThrows(IllegalArgumentException.class, () -> Category.of(null));
    }
}