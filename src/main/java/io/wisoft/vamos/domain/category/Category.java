package io.wisoft.vamos.domain.category;

import com.google.common.base.Preconditions;
import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "category")
@Getter
@SequenceGenerator(
        name = "category_sequence_generator",
        sequenceName = "category_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class Category extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence_generator")
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private CategoryName name;

    protected Category() {}

    private Category(String categoryNameEN) {
        Preconditions.checkArgument(isValidName(categoryNameEN), "존재하지 않는 카테고리 입니다.");
        this.name = CategoryName.valueOf(categoryNameEN);
    }

    private Category(Long categoryNumber) {
        CategoryName categoryName = getCategoryNameById(categoryNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 입니다."));
        this.name = CategoryName.valueOf(categoryName.getEn());
    }

    public static Category of(String categoryNameEN) {
        return new Category(categoryNameEN);
    }

    public static Category of(Long categoryNumber) {
        return new Category(categoryNumber);
    }

    private boolean isValidName(String categoryNameEN) {
        boolean isMatch = false;
        for (CategoryName name : CategoryName.values()) {
            if (name.getEn().equals(categoryNameEN)) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }

    private Optional<CategoryName> getCategoryNameById(Long categoryNumber) {
        for (CategoryName name : CategoryName.values()) {
            if (Objects.equals(name.getNo(), categoryNumber)) {
                return Optional.of(name);
            }
        }
        return Optional.empty();
    }
}
