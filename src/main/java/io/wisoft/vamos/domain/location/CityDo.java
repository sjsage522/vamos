package io.wisoft.vamos.domain.location;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "city_do")
@Getter
@SequenceGenerator(
        name = "city_do_sequence_generator",
        sequenceName = "city_do_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class CityDo extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "city_do_sequence_generator")
    private Long id;

    @Column(name = "city_do_name")
    private String name;
}
