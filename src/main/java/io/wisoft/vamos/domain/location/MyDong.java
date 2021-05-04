package io.wisoft.vamos.domain.location;

import io.wisoft.vamos.domain.BaseTimeEntity;
import io.wisoft.vamos.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "my_dong")
@Getter
public class MyDong extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "auth_count")
    private int authCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_id")
    private Dong dong;

    @Column(name = "my_dong_range")
    @Enumerated(EnumType.STRING)
    private MyDongRange myDongRange;
}
