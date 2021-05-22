package io.wisoft.vamos.domain.user;

import com.google.common.base.Preconditions;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "authority")
@Getter
public class Authority {

    @Id
    @Column(name = "authority_name")
    private String authorityName;

    protected Authority() {}

    private Authority(String authorityName) {
        Preconditions.checkArgument(checkValid(authorityName), "알 수 없는 권한 입니다.");
        this.authorityName = authorityName;
    }

    private boolean checkValid(String authorityName) {
        return authorityName.equals("ROLE_USER") || authorityName.equals("ROLE_ADMIN");
    }

    public static Authority of(String authorityName) {
        return new Authority(authorityName);
    }
}
