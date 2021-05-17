package io.wisoft.vamos.domain.user;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "user_category_keyword",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "keyword_name"})
        })
@Getter
@SequenceGenerator(
        name = "user_category_keyword_sequence_generator",
        sequenceName = "user_category_keyword_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class UserCategoryKeyword extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_category_keyword_sequence_generator")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "keyword_name")
    private String keywordName;
}
