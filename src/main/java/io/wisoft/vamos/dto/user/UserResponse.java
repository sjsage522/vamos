package io.wisoft.vamos.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.user.Authority;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Set;

@Getter
@Setter
public class UserResponse {

    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("phone_number")
    private PhoneNumber phoneNumber;

    @JsonProperty("user_roles")
    private Set<Authority> authorities;

    @JsonProperty("location")
    private UserLocation location;

    public UserResponse(User source) {
        BeanUtils.copyProperties(source, this);
    }
}
