package io.wisoft.vamos.domain.category;

import io.wisoft.vamos.domain.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
public class Category extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
