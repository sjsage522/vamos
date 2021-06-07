package io.wisoft.vamos.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "authority")
@Getter
public class Authority {

    @Id
    @Column(name = "authority_name")
    @JsonProperty("role_name")
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

    /* hashcode 가 같을 경우 동등성 비교 */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return Objects.equals(getAuthorityName(), authority.getAuthorityName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthorityName());
    }
}
