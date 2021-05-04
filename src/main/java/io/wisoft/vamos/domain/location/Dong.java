package io.wisoft.vamos.domain.location;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "dong")
@Getter
public class Dong extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dong_name")
    private String name;

    @Column(name = "x")
    private float x;

    @Column(name = "y")
    private float y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_gu_gun_id")
    private CityGuGun cityGuGun;
}
