package io.wisoft.vamos.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.wisoft.vamos.domain.user.PhoneNumber;
import io.wisoft.vamos.domain.user.User;
import io.wisoft.vamos.domain.user.UserLocation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class UserResponse {

    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("phone_number")
    private PhoneNumber phoneNumber;

    @JsonProperty("location")
    private UserLocation location;

    @JsonProperty("profile")
    private String picture;

    public UserResponse(User source) {
        BeanUtils.copyProperties(source, this);
    }
}
