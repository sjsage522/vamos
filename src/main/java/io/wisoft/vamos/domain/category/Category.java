package io.wisoft.vamos.domain.category;

import com.google.common.base.Preconditions;
import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

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

    private Category(CategoryName categoryName) {
        Preconditions.checkArgument(isValid(categoryName), "존재하지 않는 카테고리 입니다.");
        this.name = categoryName;
    }

    public static Category of(CategoryName categoryName) {
        return new Category(categoryName);
    }

    private boolean isValid(CategoryName categoryName) {
        boolean isMatch = false;
        for (CategoryName name : CategoryName.values()) {
            if (name.equals(categoryName)) {
                isMatch = true;
                break;
            }
        }
        return isMatch;
    }
}
