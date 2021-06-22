package io.wisoft.vamos.domain.category;

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
}
