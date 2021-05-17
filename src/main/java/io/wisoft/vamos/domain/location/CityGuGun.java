package io.wisoft.vamos.domain.location;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "city_gu_gun")
@Getter
@SequenceGenerator(
        name = "city_gu_gun_sequence_generator",
        sequenceName = "city_gu_gun_sequence",
        initialValue = 1,
        allocationSize = 50
)
public class CityGuGun extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "city_gu_gun_sequence_generator")
    private Long id;

    @Column(name = "city_gu_gun_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_do_id")
    private CityDo cityDo;
}
